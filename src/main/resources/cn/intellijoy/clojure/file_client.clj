;;1、如果从clojure代码中启动clojure verticle的多个instance，启动的instance共享一个clojure runtime。
;;2、启动clojure verticle的过程，相当于代码重新执行一遍，所以var和fn最好都定义成once。
;;3、即使是异步行为，verticle也是以单线程运行（？）。
;;4、所以客户端模拟并发最好通过instance数量来控制，只要计算好每个instance执行多少任务即可。
;;5、比如总共运行10000个上传，10个instance，那么传入到每个instance执行1000个即可。

(ns cn.intellijoy.clojure.file-client
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [vertx.logging :as log]
            [vertx.stream :as stream]))

(defonce file-count (atom (:total-files (core/config) 1)))
(defonce success-count (atom 0))
(defonce failure-count (atom 0))

(defn send-header
  "因为需要多文件并发测试，每个文件名必须不同，不能预先设定，最好的办法是用uuid。"
  [sock header-map]
    (let [token (.toString (java.util.UUID/randomUUID))
          tbytes (.getBytes token "ISO-8859-1")
          tlen (count (seq tbytes))
          {:keys [tag cmd-type file-len]} header-map]
      (stream/write sock tag)
      (stream/write sock cmd-type)
      (stream/write sock (short tlen))
      (stream/write sock tbytes)
      (stream/write sock file-len)))

(defprotocol ByteSourceProt
  (pause [this])
  (resume [this])
  (startRead [this]))

(deftype MockByteSource [num-atom paused-flag sock config]
  ByteSourceProt
  (pause [this] (swap! paused-flag not))
  (resume [this] (do
                   (swap! paused-flag not)
                   (.startRead this)))
  (startRead [this]
              (let [bytes-to-send (:bytes-to-send config)
                    {:keys [str-line how-many encoding] :or {encoding "ISO-8859-1"}} bytes-to-send
                    buf (buf/buffer str-line encoding)]
                (loop []
                  (when-not (or @paused-flag (>= @num-atom how-many))
                    (stream/write sock buf)
                    (when (.writeQueueFull sock)
                      (.pause this)
                      (stream/on-drain sock #(.resume this)))
                    (swap! num-atom + 1)
                    (recur))))))

(defn start-interact
  "如果目前在:start阶段，收到2个字节，0表示接下来上传文件，1表示出错了。
   如果在:header-parsed阶段，收到2个字节，0表示上传成功，1表示上传失败。
   当file-count到达1的时候，不应该再swap!它的值了。"
  [config instance-count sock buf-atom rece-state mbs buffer]
    (swap! buf-atom buf/append! buffer)
    (let [len (.length @buf-atom)
          report-to (:report-to config)]
      (condp = (:stage @rece-state "reporter")
        :start  (when (= len 2)
                  (let [res (buf/get-short @buf-atom 0)]
                    (swap! rece-state assoc :stage :header-parsed)
                    (reset! buf-atom (buf/buffer))
                    (when (= res (short 0))
                      (.startRead mbs))))
        :header-parsed (when (= len 2)
                         (let [res (buf/get-short @buf-atom 0)]
                           (swap! rece-state assoc :stage :header-parsed)
                           (reset! buf-atom (buf/buffer))
                           (if (= res (short 0))
                             (swap! success-count + 1)
                             (swap! failure-count + 1))
                           (.close sock)
                           (swap! file-count - 1)
                           (when (> @instance-count 1)
                             (swap! instance-count - 1))
                           (when (< @file-count 1)
                             (eb/send report-to {:success-count @success-count :failure-count @failure-count}))))
        nil)))



(defn create-data-handler
  "需要一个buffer atom来存储接收到的字节流，用atom，这样可以被reset。
   需要一个跟踪接受状态的atom，根据不同的值，采取不同的动作。
   需要一个MockByteSource来可控地发送字节。
   如果因为程序写入到sock过快，引起sock的.writeQueueFull，就请MockByteSource暂停，当sock通知drain时再通知MockByteSource重新发送"
  [config instance-count sock]
    (let [buf-atom (atom (buf/buffer))
          rece-state (atom {:stage :start})
          mbs (->MockByteSource (atom 0) (atom false) sock config)]
      (fn [buffer]
        (start-interact config instance-count sock buf-atom rece-state mbs buffer))))



(defn fire-one
  "fire one connect,
  rece-state keys:
      :start
      :header-parsed
  config:
      :report-to
      :header-to-send
      :bytes-to-send {:str-line 'abc\n' :how-many 1000 :encoding 'ISO-8859-1'}
      :per-instance-files
      :total-files"
  [config instance-count]
    (-> (net/client)
        (net/connect (:port config) (:host config)
                     (fn [err sock]
                       (if-not err
                         (do
                           (stream/on-data sock (create-data-handler config instance-count sock))
                           (send-header sock (:header-to-send config)))
                         (log/error err))))))

(let [config (core/config)
      cf (:per-instance-files config 1)
      instance-count (atom cf)]
  (log/info (str ":per-instance-files" cf))
  (add-watch instance-count :instance-count-watcher (fn [k atm ov nv]
                                                      (fire-one config instance-count)))
  (fire-one config instance-count))
