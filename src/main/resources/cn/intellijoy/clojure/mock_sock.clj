(ns cn.intellijoy.clojure.mock-sock
  (:import [(org.vertx.java.core.streams ExceptionSupport  Pump ReadStream WriteStream)
            (org.vertx.java.core.net NetSocket)]))

;; this will not work.because cannot see NetSocket class from here.
(defn create
  "create a mock sock."
  []
  (proxy [NetSocket] []
    (write
     [buf]
     (println buf)))
