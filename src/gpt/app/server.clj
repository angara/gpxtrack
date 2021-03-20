(ns gpt.app.server
  (:require
    [taoensso.timbre          :refer  [debug warn]]
    [mount.core               :refer  [defstate]]
    ;
    [org.httpkit.server       :refer  [run-server server-stop!]]
    [ring.util.response       :refer  [resource-response]]  ;content-type]]
    [reitit.ring              :as     ring]
    [reitit.coercion          :refer  [compile-request-coercers]]
    [reitit.coercion.malli]
    ;
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    ;
    [reitit.ring.coercion               :as coercion]
    [reitit.ring.middleware.muuntaja    :as muuntaja]
    [reitit.ring.middleware.exception   :as exception]
    [reitit.ring.middleware.multipart   :as multipart]
    [reitit.ring.middleware.parameters  :as parameters]
    ;
    [muuntaja.core]
    ;    
    [gpt.cfg                :as     cfg]
    [gpt.app.routes         :refer  [api-routes internal-routes html-routes]]
    [gpt.html.home          :refer  [h-not-found]]))
;=

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(def exception-trace-middleware
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {
       ::exception/wrap 
       (fn [handler e request]
          ;(when-not (ex-data e)
          (warn e "exception-trace:" (:uri request))
          (handler e request))})))
;-

(def CORS_HEADERS
  {"Access-Control-Allow-Origin"   "*"
   "Access-Control-Allow-Methods"  "GET, POST, OPTIONS"
   "Access-Control-Allow-Headers"  "Content-Type, Authorization"
   "Access-Control-Expose-Headers" "*"})
;-

(defn wrap-cors [handler]
  (fn [req]
    (if (= :options (:request-method req))
      {:status 200 :headers CORS_HEADERS :body ""}
      (->
       (handler req)
       (update :headers merge CORS_HEADERS)))))
;;

(defn wrap-server-name [handler server-name]
   #(-> %
      (handler)
      (update :headers merge
        { "X-ServerName"  server-name
          "X-ServerTime"  (str (System/currentTimeMillis))})))
;;

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(defn- swagger-json-route [appname version]
  ["/swagger.json"
    {:get 
      { :no-doc  true
        :handler (swagger/create-swagger-handler)
        :swagger { :info
                    { :title        appname
                      :version      version
                      :description  (str appname " api explorer")}
                    :securityDefinitions
                    { :authorization
                      { :type "apiKey"
                        :name "Authorization"
                        :in   "header"}}}}}])
  ;;

(defn- swagger-ui-handler []
  (swagger-ui/create-swagger-ui-handler
    {:path "/swagger" :config {:validatorUrl nil :operationsSorter "alpha"}}))
;;


;; (defn- index-html-route []
;;   ["/" {:get
;;           { :no-doc true
;;             :handler (fn [_] 
;;                       (-> "public/index.html"
;;                           (resource-response {})
;;                           (content-type "text/html;charset=utf-8")))}}])
;; ;;

(defn make-handler [route-list]
  (ring/ring-handler
    (ring/router 
      route-list
      {
        ;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
        ;;:validate spec/validate ;; enable spec validation for route data
        ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
        ; :exception pretty/exception
        :data
          { :coercion reitit.coercion.malli/coercion
            :compile  compile-request-coercers
            :muuntaja muuntaja.core/instance
            :middleware 
            [
              swagger/swagger-feature               ;; swagger feature
              parameters/parameters-middleware      ;; query-params & form-params
              muuntaja/format-negotiate-middleware  ;; content-negotiation
              muuntaja/format-response-middleware   ;; encoding response body
              exception-trace-middleware
              muuntaja/format-request-middleware    ;; decoding request body
              coercion/coerce-response-middleware   ;; coercing response bodys
              coercion/coerce-request-middleware    ;; coercing request parameters
              multipart/multipart-middleware]}})
    ;;
    (ring/routes
      (swagger-ui-handler)
      (ring/create-resource-handler { :path "/" :root "public"})
      (ring/create-default-handler {:not-found h-not-found}))))
;;

(comment

  (resource-response "public/index.html")
  (api-routes)

  ,)
  ;; (def app (make-handler))

  ; (app
  ;   { :uri "/api/test/qwe"
  ;     :request-method :get
  ;     :query-params {"x" "1", "y" "fail"}}))


;; https://github.com/http-kit/http-kit/blob/master/src/org/httpkit/server.clj
;; 
(defstate server
  :start
    (let [appname (:appname cfg/build)
          version (:version cfg/build)
          cf      (:http    cfg/app)
          cf (assoc cf 
              :ip                   (:host cf) 
              :worker-name-prefix   "httpkit-"
              :server-header        nil
              :legacy-return-value? false)
          route-list
            (concat
              [(swagger-json-route appname version)]
              [(api-routes)]
              [(internal-routes)]
              [(html-routes)])]
      ;
      (debug "httpkit.listener" cf)
      (-> route-list
        (make-handler)
        (wrap-server-name (str appname " " version))
        (run-server cf)))
  ;
  :stop
    (when server
      (server-stop! server {:timeout 500})))
;=
