(ns cn.intellijoy.clojure.tapp-utils
  (:require [vertx.testtools :as t]
            [vertx.logging :as log]
            [vertx.filesystem.sync :as syncfs]))

(defn verify-one
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

(defn verify-files
  "检测上传的文件是否正确"
  [path str-line how-many]
  (if (:directory? (syncfs/properties path))
    (doseq [p (syncfs/read-dir path)]
      (verify-one p str-line how-many))
    (verify-one path str-line how-many)))


(defn delete-files
  [path]
  (if (:regular-file? (syncfs/properties path))
    (syncfs/delete path)
    (do
      (doseq [p (syncfs/read-dir path)]
        (delete-files p)))))

(defn delete-folder
  "delete all regular files first.
  then delete whole empty folder."
  [path]
  (if (syncfs/exists? path)
    (do
      (delete-files path)
      (if (syncfs/exists? path)
        (syncfs/delete path true)))))

(defn before-test
  ([]
   (before-test "upload"))
  ([subdir]
   (let [path "testdatafolder"]
     (delete-folder path)
     (if-not (syncfs/exists? path)
       (syncfs/mkdir (str path "/" subdir) true)))))
