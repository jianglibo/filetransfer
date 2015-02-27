(ns cn.intellijoy.clojure.file-server
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.core :as core]
            [vertx.filesystem.sync :as syncfs]
            [cn.intellijoy.clojure.app-constants :as app-constants]
            [vertx.filesystem :as fs]
            [vertx.eventbus :as eb]
            [vertx.logging :as log]
            [vertx.stream :as stream]))


(defn parse-header
  "parse header.
      tag short
      cmd-type short
      token-length short
      token string
      file-length int
  So, when got 6 bytes, we can got total header length.
  rece-state keys:
    :stage
    :header-length
    :header
    :rec-async-file
  "
  [config sock buf-atom rece-state buffer]
    (swap! buf-atom buf/append! buffer)
    (let [len (.length @buf-atom)]
      (if (> len 5)
        (let [token-length (buf/get-short @buf-atom 4)]
          (swap! rece-state assoc :header-length (+ 2 2 2 token-length 4))
          (if (= (.length @buf-atom) (:header-length @rece-state))
            (let [token-end (+ 6 token-length)
                  header {
                          :tag (buf/get-short @buf-atom 0)
                          :cmd-type (buf/get-short @buf-atom 2)
                          :token (buf/get-string @buf-atom 6 token-end)
                          :file-length (buf/get-int @buf-atom token-end)}]
              (swap! rece-state assoc :stage :header-parsed)
              (swap! rece-state assoc :header header)
              (swap! rece-state assoc :rec-async-file (syncfs/open (str (:data-dir config) "/" (:token header)) :read? false :write? true :flush true))
              (reset! buf-atom (buf/buffer))
              (condp = (get-in @rece-state [:header :cmd-type])
                (short 0) (stream/write sock (short 0)))))))))

(defn a-d-c
  [oc]
  (reduce
   (fn [r item]
     (if-not ((item 0) r)
       (assoc r (item 0) (item 1))
       r))
   oc {:host "localhost" :port 1234 :data-dir "testdatafolder/upload"}))

(defn apply-default-cfg
  [origin-cfg]
  (let [new-config (if origin-cfg
                     (a-d-c origin-cfg)
                     (a-d-c {}))]
    (when-not (syncfs/exists? (:data-dir new-config))
      (syncfs/mkdir (:data-dir new-config) true))
    new-config))

(defn save-file
  "收到多少字节，用来决定写入文件的位置，
  写入多少字节到文件用来决定真正的上传成功。
  当sock过来的速度太快，asyncfile来不及写入到磁盘，暂停sock，让后等待asyncfile的drain信息过来时，恢复sock,
  save-file-old的错误在于使用了asyncfile的write，而不是stream的write，stream的write本身就已经维护了position的状态。
  脱离了stream的环境，on-drain就不会被正确调用，导致sock始终挂起。"
  [config sock rece-state writen-bytes-atom buffer]
    (let [asyncfile (:rec-async-file @rece-state)
          flen (get-in @rece-state [:header :file-length])
          blen (.length buffer)]
      (stream/write asyncfile buffer)
      (swap! writen-bytes-atom + blen)
      (if (= @writen-bytes-atom flen)
        (fs/close asyncfile
                  (fn [exm]
                    (if exm
                      (log/error exm)
                      (do
                        (swap! rece-state assoc :end-tms (.getTime (java.util.Date.)))
                        (stream/write sock (short 0))
                        (eb/publish app-constants/upload-finish-event-name (dissoc @rece-state :rec-async-file))))))
        (when (.writeQueueFull asyncfile)
          (.pause sock)
          (stream/on-drain asyncfile #(.resume sock))))))


(defn create-data-handler
  "返回一个函数，这个函数引用了环境变量，相当于closure"
  [config sock]
    (let [buf-atom (atom (buf/buffer))
          rece-state (atom {:stage :start :start-tms (.getTime (java.util.Date.))})
          writen-bytes-atom (atom 0)]
      (fn [buffer]
        (condp = (:stage @rece-state)
          :start (parse-header config sock buf-atom rece-state buffer)
          :header-parsed (save-file config sock rece-state writen-bytes-atom buffer)))))


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

(let [cfg (apply-default-cfg (core/config))]
  (when-not (:not-start cfg)
    (start-server cfg)))
