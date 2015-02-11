(ns cn.intellijoy.verticle.file-client
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]))


(defn send-bytes
  "此方法会占用大量内存，不利于压力测试"
  [sock bytes-to-send]
  (let [{:keys str-line how-many encoding} bytes-to-send
        buf (buf/buffer str-line encoding)]
    (dotimes [_ how-many]
      (stream/write sock buf))))

(defn parse-response
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
                    (send-bytes sock (:bytes-to-send config)))))
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
    (println "buffer length:" (.length buffer))
    (condp = (:stage @rece-state)
      :start (parse-response config sock buf-atom rece-state buffer)
      :header-parsed :>> (fn [_]))

    (if (.writeQueueFull sock)
      (do
        (.pause sock)
        (stream/on-drain #(.resume sock)))))))

(defn send-header
  [sock dv]
  (doall
    (map
      (fn [d]
        (condp = (class d)
          clojure.lang.PersistentVector (stream/write sock (first d) (last d))
          (stream/write sock d))) dv)))

(defn fire-one
  "fire one connect,
  rece-state keys:
      :start
      :header-parsed
  config:
      :reply-to
      :header-to-send
      :bytes-to-send {:str-line "abc\n" :how-many 1000 :encoding "ISO-8859-1"}
      "
[config]
    (-> (net/client)
        (net/connect 1234 "localhost"
          (fn [err sock]
            (if-not err
              (stream/on-data sock (create-data-handler config sock))
              (send-header sock (:header-to-send config)))))))

(let [config (core/config)]
  (fire-one config))
