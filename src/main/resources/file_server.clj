(ns cn.intellijoy.verticle.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.core :as core]
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
  [config sock buf-atom rece-state buffer]
  (swap! buf-atom buf/append! buffer)
  (let [len (.length @buf-atom)]
    (if (> len 5)
      (let [token-length (buf/get-short @buf-atom 4)]
        (println "token-length:" token-length)
        (swap! rece-state assoc :header-length (+ 2 2 2 token-length 4))
        (if (= (.length @buf-atom) (:header-length @rece-state))
          (let [token-end (+ 6 token-length)
                header {
                        :tag (buf/get-short @buf-atom 0)
                        :cmd-type (buf/get-short @buf-atom 2)
                        :token (buf/get-string @buf-atom 6 token-end)
                        :file-length (buf/get-int @buf-atom token-end)}]
            (swap! rece-state assoc :stage :header-parsed)
            (swap! rece-state assoc :header header)
            (reset! buf-atom (buf/buffer))
            (condp = (get-in @rece-state [:header :cmd-type])
              99 (stream/write sock (json/write-str @rece-state) "ISO-8859-1"))))))))

(defn save-file
  [config header received-bytes-atom buffer]

  )

(defn create-data-handler
  "handle all received buffers."
  [config sock]
  (let [buf-atom (atom (buf/buffer)) rece-state (atom {:stage :start}) received-bytes-atom (atom 0)]
    (fn [buffer]
      (condp = (:stage @rece-state)
        :start (parse-header config sock buf-atom rece-state buffer)
        :header-parsed (save-file config (:header @rece-state) received-bytes-atom buffer))
      (if (.writeQueueFull sock)
        (do
          (.pause sock)
          (stream/on-drain #(.resume sock)))))))

(defn conn-handler [sock config]
  (let [c-h (create-data-handler config sock)]
    (println c-h)
    (stream/on-data sock c-h)))

(defn start-server
  "config:
    :port 1234
    :host "localhost"
    :file-dir "testdatafolder/upload"
  "
  [config]
  (-> (net/server)
    (net/on-connect conn-handler config)
    (net/listen 1234 "localhost")))

(start-server (core/config))
