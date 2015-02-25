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

(def aa (atom 0))

(-> (Thread/currentThread) .getId)

(add-watch aa :akey (fn
                      [k r oldv newv]
                      (println (-> (Thread/currentThread) .getId))
                      (println oldv)
                      (println newv)))

(swap! aa + 1)

(class (alength (.getBytes "abc")))

(def c (atom 0))

(swap! c - 1)
@c

(if nil 1 2)

(re-find #"^.*(?!done)$" "a.done")

(re-find #"a(?!b)" "ab")

(= 3 (class (count "abc")))

(def sampler "0.71374246044247780.103602924582109510.155101474512006950.31245159492039130.31020293174720890.27062537680942520.0075695551627192880.068104493349678610.89287153711866570.10733860155859010.31567441167649990.77696571769112680.26527563319190240.28979742711373680.16951229157437920.183441564329675070.21622197198767590.0349159442638082850.054948457669872660.37158020610120280.27967040086418550.35190253918963310.61558199355522960.58883768589307020.75868515733257420.059397244898441780.0112006210938967450.71544428975953390.274235744402647750.52757038079752290.298048664686237140.86646221521410220.93653451829183440.48041791536468980.23377128421878890.75522733971146450.60476189419317640.350716110218585840.91868599074046560.86971918251347310.60044380356043390.61857368415289920.72507256246766650.217504749117615530.67041058229755960.284247191526076140.0374607554895983740.38025371710202850.95718013460348380.126861143327385630.4294350211876550.88542369505613360.22777255174210220.68290151699761590.0362635673464488660.45056612020284390.63637734050145210.94182757180223210.047699197904731430.48480475699922130.8519584498078320.60087324900964420.")

(.length sampler)
