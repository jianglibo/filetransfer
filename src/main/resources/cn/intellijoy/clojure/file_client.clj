(ns cn.intellijoy.clojure.file-client
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [vertx.logging :as log]
            [vertx.stream :as stream]))

(defn send-bytes
  "此方法会占用大量内存，不利于压力测试，但是dotimes是异步行为，什么时候发送reply-to是一个问题。"
  [sock config]
  (let [bytes-to-send (:bytes-to-send config)
        {:keys [str-line how-many encoding] :or {encoding "ISO-8859-1"}} bytes-to-send
        buf (buf/buffer str-line encoding)]
    (dotimes [_ how-many]
      (stream/write sock buf))
    (if-let [reply-to (:reply-to config)]
      (eb/send reply-to "done"))))

(defn start-interact
  "如果目前在:start阶段，收到2个字节，0表示接下来上传文件，1表示出错了。
  如果在:header-parsed阶段，收到2个字节，0表示上传成功，1表示上传失败。
  "
  [config sock buf-atom rece-state buffer]
  (swap! buf-atom buf/append! buffer)
  (let [len (.length @buf-atom)]
    (condp = (:stage @rece-state)
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
                           (.close sock)
                           (.close sock))))
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

(defn send-header
  [sock dv]
  (log/info "start send header...")
  (log/info dv)
  (doall
    (map
      (fn [d]
        (condp = (class d)
          clojure.lang.PersistentVector (stream/write sock (d 0) (d 1))
          (stream/write sock d))) dv)))

(defn fire-one
  "fire one connect,
  rece-state keys:
      :start
      :header-parsed
  config:
      :reply-to
      :header-to-send
      :bytes-to-send {:str-line 'abc\n' :how-many 1000 :encoding 'ISO-8859-1'}
      "
[config]
    (-> (net/client)
        (net/connect 1234 "localhost"
          (fn [err sock]
            (if-not err
              (do
                (log/info "client fired.")
                (stream/on-data sock (create-data-handler config sock))
                (send-header sock (:header-to-send config)))
              (log/error err))))))

(let [config (core/config)]
  (fire-one config))
