(ns integration.clojure.upload-itm-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.logging :as log]
            [vertx.filesystem.sync :as syncfs]
            [vertx.core :as core]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [vertx.eventbus :as eb]))


(tapp-utils/before-test)


(defn upload [ & {:keys [how-many concurrent-files total-files data-dir] :or {concurrent-files 1 total-files 1}}]
  (let [rand-line (reduce #(str %1 %2) (take 100 (repeatedly rand)))
        client-config (tapp-utils/sample-upload-data
                :report-to "test.data"
                :concurrent-files concurrent-files
                :total-files total-files
                :bytes-to-send {:str-line (str rand-line "\n") :how-many how-many})
        data-dir (str "testdatafolder/upload_" how-many "_" concurrent-files)
        server-config {:data-dir data-dir}]
    (eb/on-message
     "test.data"
     (fn [m]
       (t/assert= 10 (:success-count m))
       (t/assert= 0 (:failure-count m))
       (tapp-utils/verify-files data-dir rand-line how-many)
       (t/assert= (int total-files) (count (syncfs/read-dir data-dir)))
       (t/test-complete)))

  (core/deploy-verticle "cn/intellijoy/clojure/file_server.clj" :config server-config
    :handler (fn [err deploy-id]
               (core/deploy-verticle "cn/intellijoy/clojure/file_client.clj" :config client-config)))))

(defn test-upload-10000-10 []
  (upload :how-many 10000 :concurrent-files 3 :total-files 10))


(t/start-tests)
