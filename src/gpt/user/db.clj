(ns gpt.user.db
  (:require
    [taoensso.timbre      :refer  [debug warn]]
    [honeysql.core        :as     sql]
    [honeysql.helpers     :as     h]
    ;
    [gpt.db.core          :refer  [transact exec! exec-one!]]))
;=

; - - - - - - - - - - - - - - - - - - -

(def ^:const USER_BASE      :user_base)
(def ^:const USER_AUTH      :user_auth)
(def ^:const USER_AUTHCODE  :user_authcode)

; - - - - - - - - - - - - - - - - - - -

(defn expire-authcodes [exp]
  (->
    (h/delete-from USER_AUTHCODE)
    (h/where [:< :expire exp])
    (sql/format)
    (exec!)))
;;

; - - - - - - - - - - - - - - - - - - -
