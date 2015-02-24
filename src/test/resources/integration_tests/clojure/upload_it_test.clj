;; Copyright 2013 the original author or authors.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;      http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;; 注意core/deploy-verticle 的参数是clj文件的路径，而不是clojure的ns

(ns integration.clojure.upload-it-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.stream :as stream]
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
       (tapp-utils/verify-files data-dir rand-line how-many)
       (t/test-complete)))

  (core/deploy-verticle "cn/intellijoy/clojure/file_server.clj" :config server-config
    :handler (fn [err deploy-id]
               (core/deploy-verticle "cn/intellijoy/clojure/file_client.clj" :config client-config)))))

(defn test-upload-100000 []
  (upload :how-many 100000))

(t/start-tests)
