(ns cn.intellijoy.verticle.file-server
  (:require [vertx.net :as net]
            [vertx.stream :as stream]))

(-> (net/server)
    (net/on-connect #(stream/pump % %))
    (net/listen 1234 "localhost"
      (fn [ex _]
        (if ex
          (println ex)
          (println "Started Net echo server on localhost:1234")))))
