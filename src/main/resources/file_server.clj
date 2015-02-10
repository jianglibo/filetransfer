(ns cn.intellijoy.verticle.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [clojure.data.json :as json]
            [vertx.stream :as stream])
  (:import (cn.intellijoy.vertx.filetransfer BufferReceiver)))




(defn parse-header
  "parse header.
      tag short
      cmd-type short
      token-length short
      token string
      file-length int
  So, when got 6 bytes, we can got total header length.
  rece-state keys:
    :stage
    :header-length
    :header
  "
  [sock hbuf! rece-state buffer]
  (buf/append! hbuf! buffer)
  (println "hbuf! length:" (.length hbuf!))
  (let [len (.length hbuf!)]
    (if (> len 5)
      (let [token-length (buf/get-short hbuf! 4)]
        (println "token-length:" token-length)
        (swap! rece-state assoc :header-length (+ 2 2 2 token-length 4))
        (if (= (.length hbuf!) (:header-length @rece-state))
          (let [token-end (+ 6 token-length)
                header {
                        :tag (buf/get-short hbuf! 0)
                        :cmd-type (buf/get-short hbuf! 2)
                        :token (buf/get-string hbuf! 6 token-end)
                        :file-length (buf/get-int hbuf! token-end)}]
            (swap! rece-state assoc :header header)
            (condp = (get-in @rece-state [:header :cmd-type])
              99 (stream/write sock (json/write-str @rece-state) "ISO-8859-1"))))))))


(defn create-data-handler
  "handle all received buffers."
  [sock]
  (let [hbuf! (buf/buffer) rece-state (atom {:stage :start})]
  (fn [buffer]
    (println "buffer length:" (.length buffer))
    (condp = (:stage @rece-state)
      :start (parse-header sock hbuf! rece-state buffer)
      :header-parsed :>> (fn [_]))

    (if (.writeQueueFull sock)
      (do
        (.pause sock)
        (stream/on-drain #(.resume sock)))))))

(defn conn-handler [sock]
  (let [c-h (create-data-handler sock)]
    (println c-h)
    (stream/on-data sock c-h)))


(-> (net/server)
    (net/on-connect conn-handler)
    (net/listen 1234 "localhost"))
