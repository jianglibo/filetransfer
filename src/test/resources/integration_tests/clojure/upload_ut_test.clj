(ns integration.clojure.upload-ut-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [cn.intellijoy.clojure.file-server :as file-server]
            [cn.intellijoy.clojure.mock-sock :as mock-sock]
            [vertx.eventbus :as eb]))


(defn test-header-parser []
  (t/assert= 1 1)
  (t/test-complete))
