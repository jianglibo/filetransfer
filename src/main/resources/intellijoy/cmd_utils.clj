(ns intellijoy.cmd-utils
  (:require [vertx.net :as net]
            [vertx.buffer :as buf]
            [vertx.stream :as stream]))

(defn create-cmd []
  (-> (buf/buffer)
        (buf/append! (short 1)) ;;tag, 2 byte
        (buf/append! (short 2)) ;;cmdtype, 2 byte
        (buf/append! "1234567891" "ISO-8859-1") ;;token, 10 byte
        (buf/append! (short 10)) ;;token-length,2 byte
        (buf/append! "1234567891" "ISO-8859-1") ;;token, 10 byte
        (buf/append! (int 1000))) ;;file-length, 4 byte
    )

(defn parse-cmd
  [b]
  (assoc
    {}
    :tag (buf/get-short b 0)
    :cmdtype (buf/get-short b 2)
    :token1 (buf/get-string b 4 13 "ISO-8859-1")
    :token-length (buf/get-short b 14)
    :token2 (buf/get-string b 16 25 "ISO-8859-1")
    :file-length (buf/get-int b 26)))
