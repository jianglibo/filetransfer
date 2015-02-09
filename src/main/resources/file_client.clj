(ns cn.intellijoy.verticle.file-client
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]
            [intellijoy.cmd-utils :as utils]))


(defn write-cmd
  "向服务器提交上传文件命令，按照当前的命令一共30个byte"
  [sock]
  (let [b (utils/create-cmd)
        md (utils/parse-cmd b)]
    (println (.length b))
    (stream/write sock "hello")
    (stream/write sock b)))

(-> (net/client)
    (net/connect 1234 "localhost"
      (fn [err sock]
        (if-not err
          (do

            (println "We have connected")
            (stream/on-data sock (fn [data] (println data)))
            (println sock)
            (stream/write sock "hello")
            (stream/write sock "hello")
            (stream/write sock "hello")
            (stream/write sock "hello")
            (stream/write sock "hello")
            (stream/write sock "hello"))))))
