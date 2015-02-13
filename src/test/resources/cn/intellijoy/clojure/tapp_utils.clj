(ns cn.intellijoy.clojure.tapp-utils
  (:require [vertx.testtools :as t]))

(defn sample-upload-data
  "头部的长度由token决定，
   头部中的文件的长度必须由内容决定，需要通过计算才行。
   :bytes-to-send
      :str-line
      :how-many
  "
  [& {:keys [reply-to token bytes-to-send] :or {reply-to "test.data"}}]
  (let [tlen (count (seq (.getBytes token "ISO-8859-1")))
        flen (* (:how-many bytes-to-send) (count (seq (.getBytes (:str-line bytes-to-send) "ISO-8859-1"))))]
    {:reply-to reply-to
     :header-to-send [(short 0) (short 0) (short tlen) [token "ISO-8859-1"] (int flen)]
     :bytes-to-send {:str-line (:str-line bytes-to-send) :how-many (:how-many bytes-to-send) :encoding "ISO-8859-1"}}))


(defn verify-file
  "检测上传的文件是否正确"
  [path str-line how-many]
   (let [err-lines (atom 0)
         total-lines (atom 0)]
     (with-open [rdr (clojure.java.io/reader path)]
       (doseq [line (line-seq rdr)]
         (if-not (= str-line line)
           (swap! err-lines + 1))
         (swap! total-lines + 1)))
     (t/assert= how-many @total-lines)
     (t/assert= 0 @err-lines)))
