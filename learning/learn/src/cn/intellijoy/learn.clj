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

(:a {})

(conj [] 1)
(count [])
(class (long 1))
(class 1)
(if-not true 5 1)
(= (int 1) (long 1))
(apply assoc {} {:a 1 :b 2})

(let [cfg {}
      ncfg (cond-> cfg
        (nil? (:data-dir cfg)) (assoc :data-dir "tt")
          )]
  )
(reduce (fn [r item]
          (if-not ((first item) r)
            (assoc r (first item) (last item))
            )) {} '{:host "localhost" :port 1234})

(reduce (fn [r item]
          (if-not ((item 0) r)
            (assoc r (item 0) (item 1))
            )) {} {:host "localhost" :port 1234})
([1 2] 1)


(let [a 1]
  a)

(defn afunc
  ""
  [& {:keys [:a :b]}]
    {:a a :b b}
  )
(nil? (:a {}))
(afunc :a 1 :b 2)

(let [a 1 {:keys [x y] :or {y 7}} {:x 5} u 666]
  [x y])

(count (seq (.getBytes "abc" "ISO-8859-1")))


(get-in [0 1 2 [3 4]] [3 1])

