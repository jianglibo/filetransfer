(ns cn.intellijoy.clojure.starter
  (:require [vertx.core :refer [config deploy-verticle deploy-worker-verticle]]
            [vertx.logging :as log]))


(log/info "starter start")

(let [cfg (config)]
  ;; start the verticles that make up the app
  (log/info "hello")
  (deploy-verticle "file_server.clj"
                   :config {}
                   :instances 1))
