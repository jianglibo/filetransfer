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

(ns integration.clojure.header-test
  (:require [vertx.testtools :as t]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.core :as core]
            [vertx.eventbus :as eb]))


(defn test-file-server-header []

  (eb/on-message
   "test.data"
   (fn [m]
     (t/test-complete
      (t/assert= (int 105) (.length m)))))

  (core/deploy-verticle "file_server.clj"
    :handler (fn [err deploy-id]
               (core/deploy-verticle
                 "file_client.clj"
                  :config {:reply-to "test.data"
                           :header-to-send [(short 0) (short 99) (short 6) ["abcdef" "ISO-8859-1"] (int 1000)]}))))

(t/start-tests)
