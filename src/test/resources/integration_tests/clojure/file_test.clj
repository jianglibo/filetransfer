(ns integration.clojure.file-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [vertx.filesystem.sync :as syncfs]
            [vertx.logging :as log]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [vertx.filesystem :as fs]
            [vertx.eventbus :as eb]))


(defn before-test
  []
  (tapp-utils/delete-folder "testdatafolder")
  (syncfs/mkdir "testdatafolder"))

(before-test)

(defn test-tapp-utils []
  (let [sampler (tapp-utils/sample-upload-data :report-to "ttt.data" :bytes-to-send {:str-line "abc\n" :how-many 100})]
    (t/assert= sampler {:report-to "ttt.data"
                        :header-to-send {:tag (short 0) :cmd-type (short 0) :file-len (int 400)}
                        :bytes-to-send {:str-line "abc\n" :how-many 100 :encoding "ISO-8859-1"}})
    (t/test-complete)))

(defn sync-file-write [path]
  "write-file is oneshot action."
  (syncfs/write-file path (buf/buffer "abc"))
  (syncfs/write-file path (buf/buffer "abc"))
  (let [size (:size (syncfs/properties path))]
    (t/test-complete (t/assert= 3 size))))

(defn test-sync-file-write []
  (sync-file-write "testdatafolder/syncw.txt"))


(defn async-file-write
  [path buffer how-many-times]
  (let [f (syncfs/open path :read? false :write? true :flush true)
        blen (.length buffer)
        bstr (clojure.string/trim (buf/get-string buffer 0 blen "ISO-8859-1"))
        total-len (* blen how-many-times)
        done-times (atom 0)]
    (dotimes [i how-many-times]
      (fs/write f buffer (* i blen) (fn [ex]
                                      (swap! done-times + 1)
                                      (if (= @done-times how-many-times)
                                        (do
                                          (fs/close f)
                                          (tapp-utils/verify-file path bstr 10000)
                                          (t/test-complete
                                           (t/assert= total-len (:size (syncfs/properties path)))))))))))

(defn test-async-file-write []
  (log/info "test-async-file-write start")
  (async-file-write
   "testdatafolder/asyncw.txt"
   (buf/buffer (str (reduce
                     (fn [r item]
                       (str r item))
                     "" (take 1000 (repeat "abc")))
                    "\n")
               "ISO-8859-1") 10000))



(t/start-tests)
