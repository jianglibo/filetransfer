(ns cn.intellijoy.clojure.app-utils)

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
     :header-to-send [(short 0) (short 99) (short tlen) [token "ISO-8859-1"] (int flen)]
     :bytes-to-send {:str-line (:str-line bytes-to-send) :how-many (:how-many bytes-to-send) :encoding "ISO-8859-1"}}))
