(use 'clojure.string)

;=================================================================================================================
;utils
;=================================================================================================================
(defn evaluate [arg mapper] (.evaluate arg mapper))
(defn toString [arg] (.toString arg))
(defn diff [arg varbl] (.diff arg varbl))
(defn toStringPostfix [arg] (.toStringPostfix arg))

(defn right-divide [a b]
  (/ a (double b)))
(defn my-divide [& args] (if (= (count args) 1)
                           (right-divide 1 (first args))
                           (reduce right-divide args)))
(defn my-mean [& args] (/ (apply + args) (count args)))

(defn my-varn [& args] (let [m (apply my-mean args)
                             length (count args)]
                         (/ (apply + (mapv #(Math/pow (- m %) 2) args)) length)))
(defn my-sin [arg] (Math/sin arg))
(defn my-cos [arg] (Math/cos arg))
(defn ret [& args] (apply vector args))
(defn my-exp [& args] (Math/exp (apply double args)))
(defn my-ln [& args] (Math/log (apply double args)))
(defn get-string [args f] (trim (apply str (mapv #(str % " ") (mapv #(f %) args)))))
;=================================================================================================================
;Objects
;=================================================================================================================

(definterface IExpression
  (^Number evaluate [mapper])
  (^Object diff [varbl])
  (^String toString [])
  (^String toStringPostfix []))

(deftype JExpression [f s diff_f diff_v args]
  IExpression
  (evaluate [this mapper] (apply (.-f this) (mapv #(.evaluate % mapper) (.-args this))))
  (diff [this varbl] (apply (.diff_f this) (apply (.-diff_v this) (mapv #(.diff % varbl) (.-args this)))))
  (toString [this] (str "(" s " " (get-string (.-args this) toString) ")"))
  (toStringPostfix [this] (str "(" (get-string (.-args this) toStringPostfix) " " s ")")))

(deftype JConstant [cnst]
  IExpression
  (evaluate [this mapper] cnst)
  (diff [this varbl] (JConstant. 0))
  (toString [this] (str (.-cnst this)))
  (toStringPostfix [this] (str (.-cnst this))))

(deftype JVariable [vr]
  IExpression
  (evaluate [this mapper] (mapper (lower-case (str (first (.-vr this))))))
  (diff [this varbl] (JConstant. (if (= (.-vr this) varbl) 1 0)))
  (toString [this] (str (.-vr this)))
  (toStringPostfix [this] (str (.-vr this))))

(defn Constant [cnst] (JConstant. cnst))
(defn Variable [vr] (JVariable. vr))
(defn Negate [& args] (JExpression. - 'negate Negate ret args))
(defn Add [& args] (JExpression. + '+ Add ret args))
(defn Subtract [& args] (JExpression. - '- Subtract ret args))
(defn Multiply [& args] (JExpression. * '* Add (fn [& diffed_args]
                                             (mapv
                                               (fn [arg]
                                                 (apply Multiply (cons
                                                                   arg
                                                                   (clojure.core/replace
                                                                     {(nth args (.indexOf diffed_args arg))
                                                                      (JConstant. 1)}
                                                                     args))))
                                               diffed_args))
                                      args))

(defn mult_diff [diffed_args args] (apply Add (mapv
                                                (fn [arg]
                                                  (apply Multiply (cons
                                                                    arg
                                                                    (clojure.core/replace
                                                                      {(nth args (.indexOf diffed_args arg))
                                                                       (JConstant. 1)}
                                                                      args))))
                                                diffed_args)))

(defn Divide [& args] (JExpression. my-divide '/ Divide (fn [& diffed_args]
                                           (if (= (count args) 1)
                                             (vector
                                               (Subtract
                                                 (JConstant. 0)
                                                 (Multiply
                                                   (JConstant. 1)
                                                   (first diffed_args)))
                                               (Multiply
                                                 (first args)
                                                 (first args)))
                                             (vector
                                               (Subtract
                                                 (apply Multiply (cons
                                                                   (first diffed_args)
                                                                   (rest args)))
                                                 (Multiply
                                                   (first args)
                                                   (mult_diff
                                                     (rest diffed_args)
                                                     (rest args))))
                                               (apply Multiply (into
                                                                 (vec (rest args))
                                                                 (vec (rest args)))))))
                                    args))

(defn Mean [& args] (JExpression. my-mean 'mean Add (fn [& diffed_args] (mapv #(Divide % (JConstant. (count args))) diffed_args)) args))
(defn part-varn-diff [v arg size] (Subtract (apply Add v) (Multiply arg size)))
(defn Varn [& args] (JExpression. my-varn 'varn Add (fn [& diffed_args] (let [size (JConstant. (count args))]
                                                  (mapv
                                                    (fn [arg]
                                                      (Divide
                                                        (Multiply
                                                          size
                                                          (JConstant. 2)
                                                          (part-varn-diff diffed_args (nth diffed_args (.indexOf args arg)) size)
                                                          (part-varn-diff args arg size))
                                                        (Multiply size size size size))) args))) args))

(defn Exp [& args] (JExpression. my-exp 'exp Multiply (fn [& diffed_args] (conj diffed_args (apply Exp args))) args))
(defn Ln [& args] (JExpression. my-ln 'ln Multiply (fn [& diffed_args] (conj diffed_args (apply Divide (cons (JConstant. 1) args)))) args))

;=================================================================================================================
;Functions
;=================================================================================================================

(defn makeFunction [f] (fn [& args] (fn [mapper] (apply f (mapv #(% mapper) args)))))

(def add (makeFunction +))
(def subtract (makeFunction -))
(def multiply (makeFunction *))
(def divide (makeFunction my-divide))
(defn negate [arg] (fn [mapper] (- (arg mapper))))
(defn constant [cnt] (fn [_] cnt))
(defn variable [vr] (fn [mapper] (mapper vr)))
(def mean (makeFunction my-mean))
(def varn (makeFunction my-varn))
(def sin (makeFunction my-sin))
(def cos (makeFunction my-cos))

(def ops {
          '+ [add Add]
          '- [subtract Subtract]
          '* [multiply Multiply]
          '/ [divide Divide]
          'negate [negate Negate]
          'mean [mean Mean]
          'varn [varn Varn]
          'sin [sin]
          'cos [cos]
          'constant [constant Constant]
          'vars [variable Variable]
          'exp [0 Exp]
          'ln [0 Ln]
})

(defn parse [item id rev]
  (cond
    (number? item) ((get (ops 'constant) id) item)
    (symbol? item) ((get (ops 'vars) id)  (str item))
    :else (if (= rev 1) (apply (get (ops (first (clojure.core/reverse item))) id) (mapv #(parse % id rev) (clojure.core/reverse (rest (clojure.core/reverse item)))))
     (apply (get (ops (first item)) id) (mapv #(parse % id rev) (rest item))))
  ))

(defn super-parse [id rev] (comp #(parse % id rev) read-string))
(def parseFunction (super-parse 0 0))
(def parseObject (super-parse 1 0))
(def parseObjectPostfix (super-parse 1 1))

