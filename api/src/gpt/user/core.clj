(ns gpt.user.core
  (:require
    [taoensso.timbre      :refer  [debug warn]]
    ;
    [mlib.crypto.random   :refer  [big-rand]]
    ;
    [gpt.user.db          :refer  [expire-authcodes]]))
;=


(defn- random-code []
  (subs (format "%09d" (big-rand)) 0 9))
;-

(defn make-authcode []
  (let [code #p  (random-code)]


    ;; (expire-authcodes)
    code))

;;

(comment

  (make-authcode)

  ,)
