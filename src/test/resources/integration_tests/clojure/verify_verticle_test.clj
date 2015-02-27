(ns integration-tests.clojure.verify-verticle-test
  (:require [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.testtools :as tt]))

(defn test-deploy []
  (core/deploy-verticle "cn/intellijoy/clojure/verify_verticle.clj"
    :handler (fn [err deploy-id]
               (tt/assert-nil err)
               (tt/test-complete))))


(tt/start-tests)
