(ns gpt.app.internal
  (:require
    [taoensso.timbre          :refer  [debug warn]]))
;=

(defn wrap-require-apikey [handler apikey]
  (let [authorization-header (str "apikey " apikey)]
    (fn [req]
      (if (= authorization-header (get-in req [:headers "authorization"])) 
        (handler req)
        {:status 403 :body "wrong apikey"}))))
;;

(defn messenger-authcode [params]
  (debug "messenger-authcode:" params)
  ;; generate new authcode
  ;; insert into user_authcode (authcode, user_data)
  ;;   {:uid :msgr :last_name :first_name :username ...}
  {:status 200
   :body "{\"ok\":true}"})
;;

(defn bind-user-authcode [])
  ;; check existing user_auth (auth_type, auth_id) for msgr:uid
  ;;  return existing
  ;; or create new and return
;;
