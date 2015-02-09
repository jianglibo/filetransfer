(ns cn.intellijoy.verticle.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream])
  (:import (cn.intellijoy.vertx.filetransfer BufferReceiver)))


;;  "当接收到足够解析protocol字节之后，此函数返回解析后的protocol和剩余的bytes
;;  否则返回nil，结构see file_client.clj


(-> (net/server)
    (net/on-connect
      (fn [sock]
        (let [br! (BufferReceiver.)]
          (stream/on-data sock
            (fn [buffer]
              (.appendBuffer br! buffer)
              (if (.headerParsed br!)
                (stream/write (short 0)))

              (if (.writeQueueFull sock)
                (do
                  (.pause sock)
                  (stream/on-drain #(.resume sock)))))))))
    (net/listen 1234 "localhost"))
