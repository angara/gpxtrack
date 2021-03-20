(ns gpt.app.routes
  (:require
    [taoensso.timbre          :refer  [debug warn]]
    ;
    [reitit.coercion.schema]
    ; [reitit.ring.spec :as spec]
    [mlib.util              :refer  [not-blank?]]
    ;    
    [gpt.cfg                :as     cfg]
    [gpt.app.auth           :refer  [wrap-auth]] ;wrap-require-user wrap-require-role user-login]]))
    [gpt.html.home          :as     home]
    [gpt.app.internal       :refer  [wrap-require-apikey messenger-authcode]]))
;=

;; XXX: remove
(defn not-implemented [req]
  (debug "not implemented:" (:uri req))
  { :status 501
    :body (str "api method not implemented: " (:uri req))})
;-

(defn auth-routes []
  ["/auth"
    {:swagger {:tags ["Authorization"]}}

    ["/user-login"
      { :summary "login user, create jwt"
        :description "create new JWT token for registered user using login and password, 
                      return token, user-info, build info"
        :parameters 
          {:body 
            [:map
              {:closed true}
              [:login 
                {:title "Login" :description "User login" :json-schema/example "user-1"}
                [:fn not-blank?]]
              [:password 
                {:title "Password" :description "not empty password" :json-schema/example "qwe123"}
                [:fn not-blank?]]]}
        :post not-implemented}]])

    ;; /code/generate
    ;; /code/token    ;; get token from authcode

    ;; ["/get-token"
    ;;   { :summary "get authorization token"
    ;;     :description "create new JWT token for registered user using login and password (as user-login)"
    ;;     :parameters {:body {:login s/Str :password s/Str}}
    ;;     :post get-token}]])
    ;; ["/refresh-token"
    ;;   { :summary "get authorization token"
    ;;     :handler not-implemented}]])
;-


;; (defn admin-routes []
;;   ["/admin"
;;     { :swagger {:tags ["Admin"]}
;;       :summary "administrative methods, USER_ROLE_ADMIN required"
;;       :middleware [[wrap-require-role C/USER_ROLE_ADMIN]]}
;;     ["/user/list"
;;       { :summary "list of users"
;;         :description "list of users registered in system"
;;         :parameters {:query {}}
;;         :get not-implemented}]
;;     ["/user/add"
;;       { :summary "add new user"
;;         :description "XXX"
;;         :parameters {:body {}}
;;         :post not-implemented}]
;;     ["/user/password"
;;       { :summary "set user password"
;;         :description "?TBD?"  ;; XXX
;;         :parameters {:body {:user-id s/Str :new-password s/Str}}
;;         :post not-implemented}]
;;     ["/user/roles"
;;       { :summary "set user roles"
;;         :description "?TBD?"  ;; XXX
;;         :parameters {:body {:roles user-roles}}
;;         :post not-implemented}]])
;; ;-

(defn user-routes []
  ["/user"
    { :swagger {:tags ["User"]}
      :summary "personam user methods, registered user required"}
      ;:middleware [[wrap-require-user]]}
    ["/info"
      { :summary "get current user info"
        :description "current user info"}]])
        ;:get user-info}]])
;-

; - - - - - - - - - - - - - - - - - - -

(defn internal-routes []
  ["/_internal"
    { :no-doc true
      :middleware [[wrap-require-apikey (:apikey cfg/internal)]]}
    ["/messenger-authcode"
      { :parameters 
          {:body 
            [:map 
              {:closed false}
              [:uid   [:fn not-blank?]]
              [:msgr  [:fn not-blank?]]]}
        :post #(messenger-authcode (-> % :parameters :body))}]])
;;

; - - - - - - - - - - - - - - - - - - -

(defn api-routes []
  ["/api"
    { :middleware [[wrap-auth (:jwt cfg/app)]]
      ;; :responses {200 {:schema s/Any}
      ;;             201 {:schema s/Any}
      ;;             400 {:schema s/Any}
      ;;             401 {:schema s/Any}
      ;;             403 {:schema s/Any}
      ;;             500 {:schema s/Any}}
      :swagger 
        { :security [{:authorization []}]
          :securityDefinitions
            {:authorization
              {:type "apiKey"
                :name "Authorization"
                :in   "header"}}}}
        ;
    
    ;; :parameters {:query, :body, :form, :header and :path} :multipart
    ;; ["/test/path-parameter/:v"
    ;;   { :swagger {:tags ["Test"]}
    ;;     ;:coercion reitit.coercion.schema/coercion
    ;;     :summary "path-parameter summary"
    ;;     :description "path-parameter long description"
    ;;     :parameters 
    ;;     {
    ;;       :path {:v s/Int} 
    ;;       (s/optional-key :opts) s/Int}
    ;;     :get
    ;;     (fn [req]
    ;;       (prn "params:" (:parameters req))
    ;;       (throw (ex-info "text" {:response {:message "we all gonna die!"}}))
    ;;       {:status 200 :body (:parameters req)})}]
    ;; ;
    ;; ["/test/post"
    ;;   { :swagger {:tags ["Test"]}
    ;;     :name ::text-post
    ;;     :summary "test post method"
    ;;     :description "test method description"
    ;;     ;:coercion reitit.coercion.spec/coercion
    ;;     :parameters 
    ;;     {:body {:a s/Int (s/optional-key :i) (s/maybe s/Int) :s s/Str}}
    ;;     :responses {200 {:body s/Any}}
    ;;     :post
    ;;     (fn [req]
    ;;       (prn "headers:" (:headers req))
    ;;       (prn "auth:" (get-in req [:headers "authorization"]))
    ;;       {:status 200 :body (:parameters req)})}]
    ;
    (auth-routes)
    (user-routes)])
;;

#_(api-routes)

; - - - - - - - - - - - - - - - - - - -

(defn html-routes []
  ["/"
    {:get          
      { ; :no-doc true
        :handler home/h-root-page}}])
;;
