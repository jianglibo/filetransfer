(ns integration-tests.clojure.atom-verticle-test
  (:require [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.logging :as log]
            [vertx.testtools :as tt]))

(defn test-deploy []
  (let [tms (atom 0)
        instances 10]
    (eb/on-message "test.data"
                   (fn [m]
                     (swap! tms + 1)
                     (log/info (str "auto value is: " m))
                     (when (= instances @tms)
                       (tt/assert= (* instances 2) m)
                       (tt/test-complete))))
  (core/deploy-verticle "cn/intellijoy/clojure/atom_verticle.clj"
                        :instances instances
                        :handler (fn [err deploy-id]
                                   (tt/assert-nil err) ))))


(tt/start-tests)
