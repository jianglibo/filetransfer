(ns cn.intellijoy.clojure.after-upload
  (:require [vertx.eventbus :as eb]
            [cn.intellijoy.clojure.app-constants :as app-constants]
            [vertx.logging :as log]))

(eb/on-message app-constants/upload-finish-event-name
  (fn [message]
    nil))
