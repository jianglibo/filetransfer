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

(ns integration-tests.clojure.config-verticle
  (:require [vertx.core :as core]
            [vertx.eventbus :as eb]
            [vertx.testtools :as tt]))

(let [config (core/config)]
  (tt/assert= Short (class (:short config)))
  (tt/assert= Integer (class (:int config)))
;;  (tt/assert= Integer (class (:buffer config))) buffer not allowed,function not allowed
  )

;;(tt/assert= short (:shortfn config)) cannot pass clojure function

(eb/send "test.data" "stoped")
