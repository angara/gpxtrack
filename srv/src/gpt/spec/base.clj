(ns gpt.spec.base
  (:require
    [clojure.string       :refer  [blank?]]
    [clojure.spec.alpha   :as     s]))
;=

(defn not-blank? [s]
  (and (string? s) (not (blank? s))))
;;

(s/def ::str->int
  (s/conformer
    (fn [v]
      (if (string? v)
        (try
          (Integer/parseInt v)
          (catch Exception _ ::s/invalid))
        v))))
;;

(defn conform! [spec data & [message]]
  (let [res (s/conform spec data)]
    (if (not= res ::s/invalid)
      res
      (throw 
        (ex-info (or message "invalid spec")
          (s/explain-data spec data))))))
;;

(s/def  ::not-blank   not-blank?)

(s/def  ::url         not-blank?)
(s/def  ::host        not-blank?)
(s/def  ::port        (s/and ::str->int pos-int?))
(s/def  ::timeout     (s/and ::str->int nat-int?))

(s/def  ::username    not-blank?)

; - - - - - - - - - - - - - - - - - - -

(s/def  ::appname    not-blank?)
(s/def  ::version    not-blank?)
(s/def  ::commit     not-blank?)
(s/def  ::timestamp  not-blank?)

(s/def  ::build
  (s/keys
    :req-un [::appname ::version ::commit ::timestamp]))
;;
