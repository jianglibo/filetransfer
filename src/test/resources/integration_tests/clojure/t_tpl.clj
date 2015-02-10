;;(defn test-deploy-worker []
;;  (eb/on-message
;;   "test.data"
;;   (fn [m]
;;     (t/test-complete
;;      (t/assert= "started" m))))
;;
;;  (core/deploy-worker-verticle "integration_tests/clojure/child.clj"
;;                               :config {:ham "biscuit"}))

;;(defn test-deploy []
;;  (eb/on-message
;;   "test.data"
;;   (fn [m]
;;     (t/test-complete
;;      (t/assert= "started" m))))
;;
;;  (core/deploy-verticle "integration_tests/clojure/child.clj"
;;                        :config {:ham "biscuit"}))
;;
;;(defn test-deploy-with-handler []
;;  (core/deploy-verticle
;;   "integration_tests/clojure/child.clj"
;;   :config {:ham "biscuit"}
;;   :handler (fn [err id]
;;              (t/test-complete
;;               (t/assert-nil err)
;;               (t/assert-not-nil id)))))
;;
;;(defn test-deploy-failure []
;;  (core/deploy-verticle
;;   "integration_tests/clojure/does_not_exist.clj"
;;   :handler (fn [err id]
;;              (t/test-complete
;;               (t/assert-not-nil err)
;;               (t/assert-nil id)))))
;;
;;(defn test-undeploy []
;;  (eb/on-message
;;   "test.data"
;;   (fn [m]
;;     (when (= "stopped" m)
;;       (t/test-complete))))
;;
;;  (core/deploy-verticle
;;   "integration_tests/clojure/child.clj"
;;   :config {:ham "biscuit"}
;;   :handler (fn [err id]
;;              (t/assert-not-nil id)
;;              (core/undeploy-verticle id))))
;;
;;(defn test-undeploy-with-handler []
;;  (core/deploy-verticle
;;   "integration_tests/clojure/child.clj"
;;   :config {:ham "biscuit"}
;;   :handler (fn [err id]
;;              (t/assert-not-nil id)
;;              (core/undeploy-verticle
;;               id
;;               (fn [err]
;;                 (t/test-complete
;;                  (t/assert-nil err)))))))
;;
;;(defn test-undeploy-failure []
;;  (core/undeploy-verticle
;;   "not-deployed"
;;   (fn [err]
;;     (t/test-complete
;;      (t/assert-not-nil err)))))
