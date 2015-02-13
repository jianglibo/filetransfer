(ns cn.intellijoy.clojure.file-server-include
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.core :as core]
            [clojure.data.json :as json]
            [vertx.filesystem.sync :as syncfs]
            [vertx.filesystem :as fs]
            [vertx.logging :as log]
            [vertx.stream :as stream]))

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
    :rec-async-file
  "
  [config sock buf-atom rece-state buffer]
  (swap! buf-atom buf/append! buffer)
  (let [len (.length @buf-atom)]
    (if (> len 5)
      (let [token-length (buf/get-short @buf-atom 4)]
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
            (swap! rece-state assoc :rec-async-file (syncfs/open (str (:data-dir config) "/" (:token header)) :read? false :write? true :flush true))
            (reset! buf-atom (buf/buffer))
            (condp = (get-in @rece-state [:header :cmd-type])
              0 (stream/write sock (short 0)))))))))
