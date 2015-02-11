(ns cn.intellijoy.learn)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(+ 1 1)

(defn java-ob
  [job]
  (println job))

(defn java-ob-sender
  []
  (let [d (java.util.Date.)]
    (println d)
    (Thread/sleep 1000)
    (java-ob d)))

(java-ob-sender)

 (:abc {:abc 1})

 ({:abc 1} :abc)

 (= 1 (:abc {:abc 1 :x 2}))

 (+ 1 2 3)

 (int 1000)

 (class (short 1000))

 (-> []
     (conj 1)
     (conj 2))
(string? 1)

(class "abc")

(class [1])
(nth [1 2] 1)
(nth [1 2] 0)
(last [1 2 3])

(class short)

(type short)

(:a {} 5)

(conj [] 1)
(count [])

