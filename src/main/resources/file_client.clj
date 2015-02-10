(ns cn.intellijoy.verticle.file-client
  (:require [vertx.net :as net]
            [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]))


(defn send-data
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
      :data-to-send
      "
[config]
  (let [cbuf! (buf/buffer) rece-state (atom {:stage :start})]
    (-> (net/client)
        (net/connect 1234 "localhost"
          (fn [err sock]
            (if-not err
              (do
                (stream/on-data sock
                  (fn [data]
                    (if-let [rp (:reply-to config)]
                      (eb/send rp data)
                      (println data))))
                (send-data sock (:data-to-send config)))))))))

(let [config (core/config)]
  (fire-one config))
