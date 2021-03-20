(ns mlib.http.resp)
  ;; (:require
  ;;   [taoensso.timbre  :refer  [warn]]))
;=

(def ^:const ERROR_RESPONSE_STATUS 200)

(defn ok [body]
  {:status 200
   :body body})
;;

(defn bad [body]
  {:status 400
   :body body})
;;

(defn err [body]
  {:status 500
   :body body})
;;

;; reitit middleware related functions
;; 

(defn throw-response! [message response]
  (throw
    (ex-info message
      { :type :reitit.ring/response
        :response response})))
;;

(defn throw-error! [message data]
  (let [err (if (:message data) data (assoc data :message message))]
    (throw-response! message
      { :status ERROR_RESPONSE_STATUS
        :body {:error err}})))
;;
