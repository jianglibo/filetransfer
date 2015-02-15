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

(ns integration.clojure.upload-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.filesystem.sync :as syncfs]
            [vertx.core :as core]
            [cn.intellijoy.clojure.tapp-utils :as tapp-utils]
            [vertx.eventbus :as eb]))


(defn upload [how-many]
  (tapp-utils/before-test)
  (let [config (tapp-utils/sample-upload-data
                :report-to "test.data"
                :bytes-to-send {:str-line "hello\n" :how-many how-many})]
    (eb/on-message
     "test.data"
     (fn [m]
       (tapp-utils/verify-files "testdatafolder/upload" "hello" how-many)
       (t/test-complete)))

  (core/deploy-verticle "cn/intellijoy/clojure/file_server.clj"
    :handler (fn [err deploy-id]
               (core/deploy-verticle "cn/intellijoy/clojure/file_client.clj" :config config)))))

(defn test-upload-100 []
  (upload 100))

(defn test-upload-10000 []
  (upload 10000))

(t/start-tests)
