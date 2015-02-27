(ns cn.intellijoy.clojure.atom-verticle
  (:require [vertx.testtools :as t]
            [vertx.eventbus :as eb]
            [vertx.logging :as log]))


(defonce an-atom (atom 0))

(swap! an-atom + 1)
(swap! an-atom + 1)

(eb/send "test.data" @an-atom)
