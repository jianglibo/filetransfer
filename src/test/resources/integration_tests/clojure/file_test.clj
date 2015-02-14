(ns integration.clojure.file-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [vertx.filesystem.sync :as syncfs]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [vertx.filesystem :as fs]
            [vertx.eventbus :as eb]))

(defn test-tapp-utils []
  (let [sampler (tapp-utils/sample-upload-data :report-to "ttt.data" :token "hello-token" :bytes-to-send {:str-line "abc\n" :how-many 100})]
    (t/assert= sampler {:report-to "ttt.data"
                        :header-to-send [(short 0) (short 0) (short 11) ["hello-token" "ISO-8859-1"] (int 400)]
                        :bytes-to-send {:str-line "abc\n" :how-many 100 :encoding "ISO-8859-1"}})
    (t/test-complete)))


(defn sync-file-write [path]
  "write-file is oneshot action."
  (syncfs/write-file path (buf/buffer "abc"))
  (syncfs/write-file path (buf/buffer "abc"))
  (let [size (:size (syncfs/properties path))]
    (t/test-complete
     (t/assert= 3 size))))

(defn test-sync-file-write []
  (sync-file-write "testdatafolder/syncw.txt"))


(defn async-file-write [path buffer how-many-times]
  (if-not (syncfs/exists? "testdatafolder")
    (syncfs/mkdir "testdatafolder"))
  (if (syncfs/exists? path)
    (syncfs/delete path))
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
  (async-file-write "testdatafolder/asyncw.txt" (buf/buffer (str (reduce (fn [r item] (str r item)) "" (take 1000 (repeat "abc"))) "\n") "ISO-8859-1") 10000))

(t/start-tests)
