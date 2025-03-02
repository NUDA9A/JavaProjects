(defn vop [f] (fn [a b] (mapv f a b)))

(def v+ (vop +))
(def v* (vop *))
(def v- (vop -))
(def vd (vop /))

(defn vop2 [f] (fn [a b] (mapv #(f % b) a)))

(def v*s (vop2 (fn [x b] (* x b))))

(defn scalar [a b] (apply + (v* a b)))

(defn transpose [a] (apply mapv vector a))

(def m*v (vop2 (fn [x b] (scalar x b))))
(def m*s (vop2 (fn [x b] (v*s x b))))
(def m*m (vop2 (fn [x b] (m*v (transpose b) x))))

(def m+ (vop v+))
(def m- (vop v-))
(def m* (vop v*))
(def md (vop vd))

(defn vect [a b] (vector (- (* (get a 1) (get b 2)) (* (get a 2) (get b 1)))
                         (- (* (get a 2) (get b 0)) (* (get a 0) (get b 2)))
                         (- (* (get a 0) (get b 1)) (* (get a 1) (get b 0)))))

(def c+ (vop m+))
(def c- (vop m-))
(def c* (vop m*))
(def cd (vop md))
