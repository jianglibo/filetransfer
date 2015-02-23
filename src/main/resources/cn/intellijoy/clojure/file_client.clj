;;作为测试客户端，可以指定以下参数：
;;  几个并发发送？
;;  总共发送几个文件？
;;  每个文件的长度？
;;  结果如何校验？（这个在测试文件中实现）
;;作为异步系统，着重需要注意以下几点：
;;  什么时候才算测试结束？

(ns cn.intellijoy.clojure.file-client
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [cn.intellijoy.clojure.file-client-include :as fci]
            [vertx.logging :as log]
            [vertx.stream :as stream]))

(def file-count (atom 0))
(def success-count (atom 0))
(def failure-count (atom 0))

(defn send-bytes
  "此方法会占用大量内存，不利于压力测试，但是dotimes是异步行为，什么时候发送report-to是一个问题。"
  [sock config]
  (let [bytes-to-send (:bytes-to-send config)
        {:keys [str-line how-many encoding] :or {encoding "ISO-8859-1"}} bytes-to-send
        buf (buf/buffer str-line encoding)]
    (dotimes [_ how-many]
      (stream/write sock buf)) ))

(defn start-interact-old
  "如果目前在:start阶段，收到2个字节，0表示接下来上传文件，1表示出错了。
  如果在:header-parsed阶段，收到2个字节，0表示上传成功，1表示上传失败。
  当file-count到达1的时候，不应该再swap!它的值了。
  "
  [config sock buf-atom rece-state buffer]
  (swap! buf-atom buf/append! buffer)
  (let [len (.length @buf-atom)
        report-to (:report-to config)]
    (condp = (:stage @rece-state "reporter")
      :start  (if (= len 2)
                (let [res (buf/get-short @buf-atom 0)]
                  (swap! rece-state assoc :stage :header-parsed)
                  (reset! buf-atom (buf/buffer))
                  (if (= res (short 0))
                    (send-bytes sock config))))
      :header-parsed (if (= len 2)
                       (let [res (buf/get-short @buf-atom 0)]
                         (swap! rece-state assoc :stage :header-parsed)
                         (reset! buf-atom (buf/buffer))
                         (if (= res (short 0))
                           (swap! success-count + 1)
                           (swap! failure-count + 1))
                         (.close sock)
                         (if (= @file-count 1)
                           (eb/send report-to {:success-count @success-count :failure-count @failure-count}))
                         (if (> @file-count 1)
                           (swap! file-count - 1))))
      nil)))


(defn start-interact
  "如果目前在:start阶段，收到2个字节，0表示接下来上传文件，1表示出错了。
  如果在:header-parsed阶段，收到2个字节，0表示上传成功，1表示上传失败。
  当file-count到达1的时候，不应该再swap!它的值了。
  "
  [config sock buf-atom rece-state mbs buffer]
  (swap! buf-atom buf/append! buffer)
  (let [len (.length @buf-atom)
        report-to (:report-to config)]
    (condp = (:stage @rece-state "reporter")
      :start  (if (= len 2)
                (let [res (buf/get-short @buf-atom 0)]
                  (swap! rece-state assoc :stage :header-parsed)
                  (reset! buf-atom (buf/buffer))
                  (if (= res (short 0))
                    (.startRead mbs))))
      :header-parsed (if (= len 2)
                       (let [res (buf/get-short @buf-atom 0)]
                         (swap! rece-state assoc :stage :header-parsed)
                         (reset! buf-atom (buf/buffer))
                         (if (= res (short 0))
                           (swap! success-count + 1)
                           (swap! failure-count + 1))
                         (.close sock)
                         (if (= @file-count 1)
                           (eb/send report-to {:success-count @success-count :failure-count @failure-count}))
                         (if (> @file-count 1)
                           (swap! file-count - 1))))
      nil)))


(defn create-data-handler
  "需要一个buffer atom来存储接收到的字节流，用atom，这样可以被reset。
  需要一个跟踪接受状态的atom，根据不同的值，采取不同的动作。
  需要一个MockByteSource来可控地发送字节。"
  [config sock]
  (let [buf-atom (atom (buf/buffer)) rece-state (atom {:stage :start}) mbs (cn.intellijoy.clojure.file-client-include.MockByteSource. (atom 0) (atom false) sock config)]
    (fn [buffer]
      (log/info (str "receive from server." (.length buffer)))
      (start-interact config sock buf-atom rece-state mbs buffer)
      (if (.writeQueueFull sock)
        (do
          (.pause mbs)
          (stream/on-drain #(.resume mbs)))))))


(defn fire-one
  "fire one connect,
  rece-state keys:
      :start
      :header-parsed
  config:
      :report-to
      :header-to-send
      :bytes-to-send {:str-line 'abc\n' :how-many 1000 :encoding 'ISO-8859-1'}
      :concurrent-files
      :total-files
      "
[config]
    (-> (net/client)
        (net/connect (:port config) (:host config)
          (fn [err sock]
            (if-not err
              (do
                (log/info (str "client fired. Thread Id: " (-> (Thread/currentThread) .getId)))
                (stream/on-data sock (create-data-handler config sock))
                (fci/send-header sock (:header-to-send config)))
              (log/error err))))))

;;要达成这样一种效果：
;;开始时启动并发个数的链接，然后当一个链接结束时，启动一个新的链接，使得连接数保持在指定的并发数。
;;当还剩下concurrent-files数的时候，watch不应该再启动了，进入收尾阶段，剩下的并发逐渐完成即可。
(let [config (core/config)
      cf (:concurrent-files config)]
  (reset! file-count (:total-files config))
  (add-watch file-count :upload-end-listener
             (fn [k r oldv newv]
               (log/info (str "watch be called ........" newv))
               (if (>= newv cf)
                 (fire-one config))))
  (dotimes [_ cf]
    (fire-one config)))

(log/info (str "file-client verticle started. Thread Id: " (-> (Thread/currentThread) .getId)))
