;;在压力测试中，我们仅仅校验文件的长度是否一致即可
;;为了统计需要，可以在消息中携带整个rece-state数据。
;;我们将这些数据保存在指定的文件中，提供给分析程序。
;;包括：文件长度，开始时间，结束时间。

(ns cn.intellijoy.clojure.verify-verticle
  (:require [vertx.eventbus :as eb]
            [vertx.core :as core]
            [clojure.java.io :as io]
            [cn.intellijoy.clojure.app-constants :as app-constants]
            [vertx.logging :as log]))

(let [f (io/file "testdatafolder" "report")]
  (when-not (.exists f)
  (.mkdirs f)))

(def rf (io/writer (io/file "testdatafolder" "report" (str "report-" (-> (java.text.SimpleDateFormat. "yyyy-MM-dd-HHmmss")
                                      (.format (java.util.Date.))) ".txt")) :append true))

(def uploaded-num (atom 0))

(defn verify-one
  [path rece-state]
  (let [filename (get-in rece-state [:header :token])
        f (io/file path filename)
        len (.length f)]
    (if-not (= len (get-in rece-state [:header :file-length]))
      (log/info "wrong file length.")
      (io/delete-file f))))


(let [config (core/config)
      data-dir (:data-dir config)
      bm-total-files (:bm-total-files config)]
  (eb/on-message app-constants/upload-finish-event-name
                 (fn [message]
                   (log/info (str "got file " @uploaded-num))
                   (verify-one data-dir message)
                   (.write rf (str message))
                   (.newLine rf)
                   (swap! uploaded-num + 1)
                   (when (>= @uploaded-num bm-total-files)
                     (.close rf)
                     (log/info (str "total upload " @uploaded-num))))))
