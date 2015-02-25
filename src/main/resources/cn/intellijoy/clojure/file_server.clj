(ns cn.intellijoy.clojure.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.core :as core]
            [clojure.data.json :as json]
            [vertx.filesystem.sync :as syncfs]
            [cn.intellijoy.clojure.file-server-include :as fsi]
            [cn.intellijoy.clojure.app-constants :as app-constants]
            [vertx.filesystem :as fs]
            [vertx.eventbus :as eb]
            [vertx.logging :as log]
            [vertx.stream :as stream]))


(defn save-file
  "收到多少字节，用来决定写入文件的位置，
  写入多少字节到文件用来决定真正的上传成功。
  当sock过来的速度太快，asyncfile来不及写入到磁盘，暂停sock，让后等待asyncfile的drain信息过来时，恢复sock,
  save-file-old的错误在于使用了asyncfile的write，而不是stream的write，stream的write本身就已经维护了position的状态。
  脱离了stream的环境，on-drain就不会被正确调用，导致sock始终挂起。"
  [config sock rece-state received-bytes-atom writen-bytes-atom buffer]
  (let [asyncfile (:rec-async-file @rece-state)
        flen (get-in @rece-state [:header :file-length])
        pos @received-bytes-atom
        blen (.length buffer)]
    (stream/write asyncfile buffer)
    (swap! received-bytes-atom + blen)
    (swap! writen-bytes-atom + blen)
    (if (= @writen-bytes-atom flen)
      (fs/close asyncfile
                (fn [exm]
                  (if exm
                    (log/error exm)
                    (do
                      (stream/write sock (short 0))
                      (eb/send app-constants/upload-finish-event-name {:filename (get-in @rece-state [:header :token])})))))
      (when (.writeQueueFull asyncfile)
        (.pause sock)
        (stream/on-drain asyncfile #(.resume sock))))))


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
        :header-parsed (save-file config sock rece-state received-bytes-atom writen-bytes-atom buffer)))))

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


(start-server (fsi/apply-default-cfg (core/config)))
