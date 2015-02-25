(ns cn.intellijoy.clojure.starter
  (:require [vertx.core :refer [config deploy-verticle deploy-worker-verticle]]
            [cn.intellijoy.clojure.sampler :as sampler]
            [cn.intellijoy.clojure.app-utils :as app-utils]
            [vertx.logging :as log]))


(let [cfg (config)
      bts (:bytes-to-send cfg)
      bytes-to-send (if (:str-line bts)
                      bts
                      (assoc bts :str-line sampler/str-line))]



  ;; start the verticles that make up the app
  (if (:client cfg)
   (deploy-verticle "cn/intellijoy/clojure/file_client.clj"
                   :config (app-utils/sample-upload-data :current-files (:current-files cfg)
                                                         :total-files (:total-files cfg)
                                                         :host (:host cfg)
                                                         :port (:port cfg)
                                                         :bytes-to-send bytes-to-send)
                   :instances (:instances cfg 1))
    (do
      (when (:verify-verticle cfg)
        (deploy-verticle "cn/intellijoy/clojure/verify_verticle.clj" :instances 1))
      (deploy-verticle "cn/intellijoy/clojure/after_upload.clj" :instances 1)
      (deploy-verticle "cn/intellijoy/clojure/file_server.clj"
                       :config cfg
                       :instances (:instances cfg 1)))))
