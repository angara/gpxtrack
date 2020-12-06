(ns gpt.web.lib.api
  (:require
    [taoensso.timbre  :refer-macros [debug warn]]
    [re-frame.core    :as rf]
    [ajax.core        :refer [GET POST]]))    
;=

(def INIT_STATE
  {:build {:appname "gpt-web" :version "Dev.0.0"}
   :api/loading nil})
;=

(def ^:const TIMEOUT 10000)

(def bearer* (atom nil))

(defn set-bearer! [val]
  (reset! bearer* val))
;;


(def METHOD_MAP
  { :api/user-login   [POST "/api/auth/user-login"]
    :api/user-info    [GET  "/api/user/info"]
    ;
    :api/docs-get    [POST "/api/docs/get"]       ;; by-id
    :api/docs-find   [POST "/api/docs/find"]  ;; id doc
    ;
    :api/system-info [GET  "/api/system-info"]})
;=

;; https://github.com/JulianBirch/cljs-ajax
;; 
(defn api-call [method params ok-event-id err-event-id]
  (let [[http url] (get METHOD_MAP method)]
    (if-not http
      (do
        (warn "api-call unexpected method:" {:method method})
        (rf/dispatch [err-event-id :internal]))
      (let [headers (when-let [b @bearer*]
                      {"Authorization" (str "Bearer " b)})]
        (http url
          { :headers  headers
            :params   params
            :timeout  TIMEOUT
            :format   :json
            :response-format :json
            :keywords? true
            :handler
            (fn [resp]
              (debug "api-call/resp:" resp)
              (rf/dispatch [ok-event-id resp]))
            :error-handler
            (fn [resp]
              (debug "api-call/err-resp:" resp)
              (rf/dispatch [err-event-id resp]))})))))
;;                            

(rf/reg-fx
  :api/call
  (fn [[method params ok-fx err-fx]]
    (debug "api-fx:" method params)
    (api-call method params ok-fx (or err-fx :api/error))))
;;

; default api error handler
(rf/reg-event-fx
  :api/error
  (fn [_ [_ error]]
    (warn "api/error" error)))
;;
