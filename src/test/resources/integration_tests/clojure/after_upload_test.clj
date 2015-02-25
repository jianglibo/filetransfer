(ns integration-tests.clojure.after-upload-test
  (:require [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.testtools :as tt]))

(defn test-deploy []
  (core/deploy-verticle "cn/intellijoy/clojure/after_upload.clj"
    :handler (fn [err deploy-id]
               (tt/assert-nil err)
               (tt/test-complete))))


(tt/start-tests)
