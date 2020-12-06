(ns gpt.web.user.core
  (:require
    [taoensso.timbre  :refer-macros [debug warn]]
    [re-frame.core    :as rf]
    [gpt.web.lib.api :refer [set-bearer!]]))
;=

(def INIT_STATE
  { :user 
    { :username nil
      :roles []
      :jwt nil}})
;;


(rf/reg-event-fx
  :user/login
  (fn [{_db :db} _]
    ;; XXX
    { :api/call 
      [:api/user-login {:login "admin" :password "1"} ::user-login-ok]}))
;-

(rf/reg-event-fx
  ::user-login-ok
  (fn [{db :db} [_ {:keys [jwt user]}]]
    (debug "::user-login-ok:" user)
    (set-bearer! jwt)
    {:db (assoc db :user user)}))
;-


(defn user-bar []
  [:div.absolute.bg-white.rounded.py-1.px-3.shadow-md.opacity-75
    {:style {:top "0.7rem" :right "0.8rem" :z-index 1000}}
    "User"])
;;
