(ns cn.intellijoy.clojure.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.core :as core]
            [clojure.data.json :as json]
            [vertx.filesystem.sync :as syncfs]
            [cn.intellijoy.clojure.file-server-include :as fsi]
            [vertx.filesystem :as fs]
            [vertx.logging :as log]
            [vertx.stream :as stream]))


(defn save-file
  "收到多少字节，用来决定写入文件的位置，
  写入多少字节到文件用来决定真正的上传成功。"
  [config sock rece-state received-bytes-atom writen-bytes-atom buffer]
  (let [asyncfile (:rec-async-file @rece-state)
        flen (get-in @rece-state [:header :file-length])
        pos @received-bytes-atom
        blen (.length buffer)]
    (swap! received-bytes-atom + blen)
    (fs/write asyncfile buffer pos
              (fn [ex]
                (swap! writen-bytes-atom + blen)
                (if (= @writen-bytes-atom flen)
                  (fs/close asyncfile
                            (fn [exm]
                              (if exm
                                (log/error exm)
                                (stream/write sock (short 0))))))))))

(defn create-data-handler
  "返回一个函数，这个函数引用了环境变量，相当于closure"
  [config sock]
  (let [buf-atom (atom (buf/buffer))
        rece-state (atom {:stage :start})
        received-bytes-atom (atom 0)
        writen-bytes-atom (atom 0)]
    (fn [buffer]
      (condp = (:stage @rece-state)
        :start (fsi/parse-header config sock buf-atom rece-state buffer)
        :header-parsed (save-file config sock rece-state received-bytes-atom writen-bytes-atom buffer))
      (if (.writeQueueFull sock)
        (do
          (.pause sock)
          (stream/on-drain #(.resume sock)))))))

(defn start-server
  "config:
    :port 1234
    :host 'localhost'
    :data-dir 'testdatafolder/upload'
  "
  [config]
  (-> (net/server)
    (net/on-connect (fn [sock]
                      (stream/on-data sock (create-data-handler config sock))))
    (net/listen (:port config) (:host config))))

(defn apply-default-cfg
  [origin-cfg]
  (log/info "apply default config.")
  (let [new-config (reduce
                    (fn [r item]
                      (if-not ((item 0) r)
                        (assoc r (item 0) (item 1))))
                    {} {:host "localhost" :port 1234 :data-dir "testdatafolder/upload"})]
    (if-not (syncfs/exists? (:data-dir new-config))
      (syncfs/mkdir (:data-dir new-config) true))
    new-config))

(start-server (apply-default-cfg (core/config)))
