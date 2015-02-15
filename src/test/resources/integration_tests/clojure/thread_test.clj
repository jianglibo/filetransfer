(ns integration.clojure.thread-test
  (:require [vertx.testtools :as t]
            [vertx.buffer :as buf]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [vertx.core :as core]
            [vertx.filesystem :as fs]
            [vertx.eventbus :as eb]))


(tapp-utils/before-test)


(def delay-id (delay (.getId (Thread/currentThread))))

(defn test-async-thread []
  (let [ct (.getId (Thread/currentThread))]
  (fs/open "testdatafolder/thread.file" (fn [exm ar]
                                       (t/assert= ct @delay-id)
                                       (t/test-complete)))))


(t/start-tests)
