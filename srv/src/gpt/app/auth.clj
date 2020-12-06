(ns gpt.app.auth
  (:require
    [taoensso.timbre      :refer  [debug warn]]
    [ring.middleware.jwt  :refer  [wrap-jwt]]
    ;[gpt.db.user        :refer  [update-atime]]
    [gpt.cfg              :as     cfg]
    [mlib.http.resp       :refer  [ok bad]]
    [mlib.jwt.core        :refer  [create-token]]))
    ;[gpt.app.user       :refer  [get-active-user active-user-by-id]]))
;=

;; XXX: STUB !!!
(defn active-user-by-id [user-id]
  {:id user-id
    :profile {:username "UserName"}})

(defn get-active-user [_login _password]
  (active-user-by-id "user-1"))

; - - - - - - - - - - - - - - - - - - -

(defn wrap-auth [handler jwt-opts]
  (wrap-jwt
    (fn [req]
      (let [{:keys [claims]} req]
        (if-let [user (active-user-by-id (:sub claims))]
          (do
            (debug "update-atime") ;; XXX
            ;(update-atime (:id user))
            (handler   
              (assoc req 
                :site (-> user :orgs first)
                :user (select-keys user [:id :roles :username :orgs]))))
          (handler req))))
    ;;
    jwt-opts))
;;

(defn wrap-require-user [handler]
  (fn [req]
    (if (:user req)
      (handler req)
      {:status 403 :body (str "\"Access denied. Registered user required.\"")})))
;;

(defn wrap-require-role [handler role]
  (fn [req]
    (debug "wrap-role:" (get-in req [:user :roles]))
    (if (some #(= % role) (get-in req [:user :roles]))
      (handler req)  
      { :status 403
        :body (str "\"Access denied. Role required: " role "\"")})))
;;

; - - - - - - - - - - - - - - - - - - -

;; (defn get-token [params]
;;   (let [{:keys [login password]} params
;;         user (get-active-user login password)]
;;     (if (:active user)
;;       (do
;;         (debug "get-token: ok" {:login login :user-id (:id user)})
;;         (ok { :jwt (create-token (:id user))
;;               :build cfg/build}))
;;       (do
;;         (debug "get-token: user inactive")
;;         (bad "user inactive")))))
;; ;;

(defn user-login [{{body :body} :parameters}]
  (let [jwt-cfg (:jwt cfg/app)
        {:keys [login password]} body
        user (get-active-user login password)]
    (debug "user-login:" user)
    (if (:active user)
      (do
        (debug "user-login: update-atime") ;; XXX !!!
        ; (update-atime (:id user))
        (ok {:jwt   (create-token (:id user) (:issuer jwt-cfg) jwt-cfg)
             :user  user
             :build cfg/build}))
      (do
        (debug "user-login: user inactive" login)
        (bad (str "user inactive:" login))))))
;;

(comment

  (require '[criterium.core :refer [quick-bench]])

  (let [token (str "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."
                "eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjAxNDU4MTQ3LCJpYXQiOjE1OTI4MTgxNDd9."
                "H9WXYusvxu4bxOqkG55eihEZSba6kLbioiI6aOJ19D0")
        jwt-opts  {:secret "oncab-jwt-dev-secret" :alg :HS256} 
        req   {:headers {"authorization" (str "Bearer " token)} :body ""}
        wrp   (wrap-jwt
                (fn [req] req)                  
                jwt-opts)]
    ;
    (quick-bench
      (wrp req)))

;; Evaluation count : 12258 in 6 samples of 2043 calls.
;;              Execution time mean : 52.648505 µs
;;     Execution time std-deviation : 1.266465 µs
;;    Execution time lower quantile : 50.579992 µs ( 2.5%)
;;    Execution time upper quantile : 53.772495 µs (97.5%)
;;                    Overhead used : 9.890366 ns
                    
  ,)
