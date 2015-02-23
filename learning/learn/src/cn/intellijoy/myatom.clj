(ns cn.intellijoy.myatom)


(def datom (atom (java.util.Date.)))

(.hashCode @datom)
(.hashCode @datom)

(.hashCode (reset! datom @datom))

(def dd @datom)

(defn set-time
  [date mm]
  (.setTime date mm)
  date)

(swap! datom set-time 20000)


(.hashCode @datom)



(def cc (atom 10))

(loop []
  (println @cc)
  (if (< (swap! cc - 1) 1)
    nil
    (recur)))
