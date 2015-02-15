(ns cn.intellijoy.clojure.tapp-utils
  (:require [vertx.testtools :as t]
            [vertx.logging :as log]
            [vertx.filesystem.sync :as syncfs]))

(defn sample-upload-data
  "头部的长度由token决定，
   头部中的文件的长度必须由内容决定，需要通过计算才行。
   :bytes-to-send
      :str-line
      :how-many
  "
  [& {:keys [report-to bytes-to-send concurrent-files total-files port host]
      :or
      {report-to "test.data" concurrent-files 1 total-files 1 host "localhost" port 1234}}]
  (let [flen (* (:how-many bytes-to-send) (count (seq (.getBytes (:str-line bytes-to-send) "ISO-8859-1"))))]
    {:report-to report-to
     :header-to-send {:tag (short 0) :cmd-type (short 0) :file-len (int flen)}
     :bytes-to-send {:str-line (:str-line bytes-to-send) :how-many (:how-many bytes-to-send) :encoding "ISO-8859-1"}
     :concurrent-files concurrent-files
     :total-files total-files
     :port port
     :host host}))


(defn verify-files
  "检测上传的文件是否正确"
  [path str-line how-many]
  (doseq [p (syncfs/read-dir path)]
         (let [err-lines (atom 0)
               total-lines (atom 0)]
           (with-open [rdr (clojure.java.io/reader p)]
             (doseq [line (line-seq rdr)]
               (if-not (= str-line line)
                 (swap! err-lines + 1))
               (swap! total-lines + 1)))
           (t/assert= how-many @total-lines)
           (t/assert= 0 @err-lines))))



(defn delete-folder
  [path]
  (if (syncfs/exists? path)
    (do
      (doseq [p (syncfs/read-dir path)]
              (if (:regular-file? (syncfs/properties p))
                (syncfs/delete p)
                (delete-folder p))))))


(defn before-test
  []
  (let [path "testdatafolder"]
    (delete-folder path)
    (if-not (syncfs/exists? path)
      (syncfs/mkdir path))))
