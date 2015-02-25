(ns cn.intellijoy.clojure.app-utils
  (:require [cn.intellijoy.clojure.sampler :as sampler]))

(defn sample-upload-data
  "头部的长度由token决定，
   头部中的文件的长度必须由内容决定，需要通过计算才行。
   :bytes-to-send
      :str-line
      :how-many
  "
  [& {:keys [report-to bytes-to-send concurrent-files total-files port host]
      :or
      {report-to "test.data" bytes-to-send {:str-line sampler/str-line :how-many 10} concurrent-files 1 total-files 1 host "localhost" port 1234}}]
  (let [flen (* (:how-many bytes-to-send) (alength (.getBytes (:str-line bytes-to-send) "ISO-8859-1")))]
    {:report-to report-to
     :header-to-send {:tag (short 0) :cmd-type (short 0) :file-len (int flen)}
     :bytes-to-send {:str-line (:str-line bytes-to-send) :how-many (:how-many bytes-to-send) :encoding "ISO-8859-1"}
     :concurrent-files concurrent-files
     :total-files total-files
     :port port
     :host host}))
