(ns integration-tests.clojure.cp-test
  (:require [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.logging :as log]
            [vertx.testtools :as tt])
  (:import [java.net URL URLClassLoader]))



(defn test-ture []
  (let [urls (-> (Thread/currentThread)
               .getContextClassLoader
               .getURLs)]
    (log/info (alength urls))
    (doseq [url (seq urls)]
      (log/info url)))
  (log/info "................")
  (tt/assert= 1 1)
  (tt/test-complete))


(tt/start-tests)
