(ns cn.intellijoy.tttt)

(defprotocol P
  (foo [x])
  (bar-me [x] [x y]))

(deftype Foo [a b c]
  P
  (foo [x] a)
  (bar-me [x] b)
  (bar-me [x y] (do
                  (println x)
                  (+ c y))))

(bar-me (Foo. 1 2 3) 42)
