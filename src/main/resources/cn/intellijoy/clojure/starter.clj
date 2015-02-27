(ns cn.intellijoy.clojure.starter
  (:require [vertx.core :refer [config deploy-verticle deploy-worker-verticle]]
            [cn.intellijoy.clojure.sampler :as sampler]
            [cn.intellijoy.clojure.app-utils :as app-utils]
            [vertx.logging :as log]))



(defn deploy-server-one [cfg inst-num]
  (deploy-verticle "cn/intellijoy/clojure/file_server.clj"
                       :config cfg
                       :instances 1
                       :handler (fn [err deploy-id]
                                  (when err
                                    (log/info "deploy file server failed."))
                                  (swap! inst-num - 1)
                                  (when (> @inst-num 0)
                                    (deploy-server-one cfg inst-num)))))

(defn deploy-client-one [cfg bytes-to-send inst-num]
  (deploy-verticle "cn/intellijoy/clojure/file_client.clj"
                   :config (app-utils/sample-upload-data :per-instance-files (:per-instance-files cfg)
                                                         :total-files (:total-files cfg)
                                                         :host (:host cfg)
                                                         :port (:port cfg)
                                                         :bytes-to-send bytes-to-send)
                   :instances 1
                   :handler (fn [err deploy-id]
                                  (when err
                                    (log/info "deploy file client failed."))
                                  (swap! inst-num - 1)
                                  (when (> @inst-num 0)
                                    (deploy-client-one cfg bytes-to-send inst-num)))))

(let [cfg (config)
      bts (:bytes-to-send cfg)
      inst-num (atom (:instances cfg 1))
      bytes-to-send (if (:str-line bts)
                      bts
                      (assoc bts :str-line sampler/str-line))]

  ;; start the verticles that make up the app
  (if (:client cfg)
    (deploy-client-one cfg bytes-to-send inst-num)
    (do
      (when (:verify-verticle cfg)
        (deploy-verticle "cn/intellijoy/clojure/verify_verticle.clj" :config cfg :instances 1))
      (deploy-verticle "cn/intellijoy/clojure/after_upload.clj" :instances 1)
      (deploy-server-one cfg inst-num))))
