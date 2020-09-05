(ns gpt.app.server
  (:require
    [taoensso.timbre          :refer  [debug warn]]
    [mount.core               :refer  [defstate]]
    ;
    [org.httpkit.server       :refer  [run-server server-stop!]]
    [ring.util.response       :refer  [resource-response content-type]]
    [reitit.ring              :as     ring]
    [reitit.coercion          :refer  [compile-request-coercers]]
    ;[reitit.coercion.spec]
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
            
            ; [reitit.ring.middleware.dev :as dev]
            ; [reitit.ring.spec :as spec]
            ; [spec-tools.spell :as spell]
    
    [muuntaja.core]
    ;    
    [gpt.cfg                :as     cfg]
    [gpt.app.routes         :refer  [api-routes]]))
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
   "Access-Control-Expose-Headers" "X-ServerTime, X-ServerName, *"})
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
  (fn [req]
    (->
     (handler req)
     (update :headers merge
             {"X-ServerName"  server-name
              "X-ServerTime"  (str (System/currentTimeMillis))}))))
;;

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(defn make-handler []
  (ring/ring-handler
    (ring/router
      [
        ["/swagger.json"
         {:get {:no-doc true
                :swagger {:info   
                            {:title   (:appname cfg/build)
                             :version (:version cfg/build)
                             :description "gpxtrack api explorer"}
                          :securityDefinitions
                            {:authorization
                                {:type "apiKey"
                                  :name "Authorization"
                                  :in   "header"}}}
                ;
                :handler (swagger/create-swagger-handler)}}]
        ;
        (api-routes)
        ;
        ["/" {:get
               { :no-doc true
                 :handler (fn [_] 
                            (-> "public/index.html"
                                (resource-response {})
                                (content-type "text/html;charset=utf-8")))}}]]
      ;;
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
      (swagger-ui/create-swagger-ui-handler
        {:path "/swagger"
         :config {:validatorUrl nil :operationsSorter "alpha"}})
      ;
      (ring/create-resource-handler {:path "/" :root "public"})
      (ring/create-default-handler))))
;;

(comment

  (resource-response "public/index.html")

  (def app (make-handler))

  (app
    { :uri "/api/test/qwe"
      :request-method :get
      :query-params {"x" "1", "y" "fail"}}))


;; https://github.com/http-kit/http-kit/blob/master/src/org/httpkit/server.clj      
;; 
(defstate server
  :start
    (let [server-name (str (:appname cfg/build) " " (:version cfg/build))
          cf (:http cfg/app)
          cf (assoc cf 
              :ip                   (:host cf) 
              :worker-name-prefix   "http-kit-"
              :server-header        nil
              :legacy-return-value? false)]
      ;
      (debug "http-kit.listener" cf)
      (->
        (make-handler)
        (wrap-server-name server-name)
        (run-server cf)))
  ;
  :stop
    (when server
      (server-stop! server {:timeout 500})))
;=
