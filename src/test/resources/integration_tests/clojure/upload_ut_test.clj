(ns integration.clojure.upload-ut-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [vertx.logging :as log]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [cn.intellijoy.clojure.file-server-include :as fsi]
            [vertx.eventbus :as eb])
  (:import [cn.intellijoy.vertx.filetransfer.integration.java MockSock]))


(tapp-utils/before-test)

(defn header-parser
  [token]
  (let [config {:data-dir "testdatafolder/upload"}
        buf-atom (atom (buf/buffer))
        rece-state (atom {:stage :start})
        sock (MockSock.)
        tbytes (.getBytes token "ISO-8859-1")
        tlen (short (alength tbytes))]
    (log/info "starting upload_ut_test.")
    (fsi/parse-header config sock buf-atom rece-state (buf/as-buffer (short 0)))
    (fsi/parse-header config sock buf-atom rece-state (buf/as-buffer (short 0)))
    (fsi/parse-header config sock buf-atom rece-state (buf/as-buffer tlen))
    (fsi/parse-header config sock buf-atom rece-state (buf/buffer tbytes))
    (fsi/parse-header config sock buf-atom rece-state (buf/as-buffer (int 1000)))
    (t/assert= (+ 10 tlen) (:header-length @rece-state))
    (t/assert= (short 0) (get-in @rece-state [:header :cmd-type]))
    (t/assert= :header-parsed (:stage @rece-state))
    (t/assert= (short 0) (buf/get-short (first (.getBuffers sock)) 0))))

(defn test-header-parser
  []
  (header-parser "hello")
  (header-parser "hello-diis-3829-dskdis923sdj")
  (t/test-complete))


(t/start-tests)
