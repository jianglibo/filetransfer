(ns integration.clojure.config-test
  (:require [vertx.testtools :as t]
            [vertx.buffer :as buf]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [vertx.eventbus :as eb]))


(defn test-config-value []
  (eb/on-message "test.data"
   (fn [m]
     (t/test-complete
      (t/assert= "stoped" m))))

  (core/deploy-verticle "config_verticle.clj"
                        :config {:short (short 5) :int (int 100)}))
;;buffer cannot put to jsonObject.
(t/start-tests)
