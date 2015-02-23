(ns cn.intellijoy.clojure.file-client-include
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [vertx.logging :as log]
            [vertx.stream :as stream]))

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
  (pause [this] (swap! paused-flag true))
  (resume [this] (do
                   (swap! paused-flag false)
                   (start-read)))
  (startRead [this]
              (let [bytes-to-send (:bytes-to-send config)
                    {:keys [str-line how-many encoding] :or {encoding "ISO-8859-1"}} bytes-to-send
                    buf (buf/buffer str-line encoding)]
                (loop [_]
                  (if (or @paused-flag (>= @num-atom how-many))
                    nil
                    (recur (do
                             (stream/write sock buf)
                             (swap! num-atom + 1))))))))



(defn send-bytes
  "此方法会占用大量内存，不利于压力测试，但是dotimes是异步行为，什么时候发送report-to是一个问题。"
  [sock config]
  (let [bytes-to-send (:bytes-to-send config)
        {:keys [str-line how-many encoding] :or {encoding "ISO-8859-1"}} bytes-to-send
        buf (buf/buffer str-line encoding)]
    (dotimes [_ how-many]
      (stream/write sock buf)) ))
