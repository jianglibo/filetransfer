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

(defn start-interact
  "如果目前在:start阶段，收到2个字节，0表示接下来上传文件，1表示出错了。
  如果在:header-parsed阶段，收到2个字节，0表示上传成功，1表示上传失败。
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
                         (swap! file-count - 1)
                         (if-not (> @file-count 0)
                           (eb/send report-to "done"))))
      nil)))


(defn create-data-handler
  "handle all received buffers."
  [config sock]
  (let [buf-atom (atom (buf/buffer)) rece-state (atom {:stage :start})]
    (fn [buffer]
      (log/info (str "receive from server." (.length buffer)))
      (start-interact config sock buf-atom rece-state buffer)
      (if (.writeQueueFull sock)
        (do
          (.pause sock)
          (stream/on-drain #(.resume sock)))))))


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
(let [config (core/config)]
    (reset! file-count (:total-files config))
    (dotimes [_ (:concurrent-files config)]
      (fire-one config))
  (add-watch file-count :upload-end-listener
             (fn [k r oldv newv]
               (fire-one config))))

(log/info (str "file-client verticle started. Thread Id: " (-> (Thread/currentThread) .getId)))
