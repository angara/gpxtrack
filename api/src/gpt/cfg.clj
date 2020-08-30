(ns gpt.cfg
  (:require
    [clojure.spec.alpha     :as       s]
    [mount.core             :refer    [defstate args]]
    [medley.core            :refer    [deep-merge]]
    [ring.middleware.token  :as       token]
    [gpt.spec.base          :as       b]))
;=

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

;; defined and checked in app/server
(s/def  ::http
  (s/map-of keyword? any?))

(s/def  ::psql
  (s/keys 
    :req-un [::b/url]))

(s/def :jwt-config/exp  pos?)

(s/def ::jwt-opts 
  (s/keys 
    :req-un [::token/alg ::token/leeway-seconds ::token/secret :jwt-config/exp]))

(s/def ::issuers
  (s/map-of ::b/not-blank ::jwt-opts))

(s/def ::issuer ::b/not-blank)

(s/def ::jwt
  (s/keys :req-un [::issuer ::issuers]))

(s/def  ::api-url   ::b/not-blank)
(s/def  ::bearer    ::b/not-blank)
(s/def  ::hostname  ::b/not-blank)

(s/def ::gpt
  (s/keys
    :req-un [::psql ::jwt]))    ;; XXX

(s/def  ::conf
  (s/keys
    :req-un [::gpt ::build]))

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(declare  app)
(declare  build)
(declare  psql)

(defstate conf
  :start
    (let [conf (b/conform! ::conf (apply deep-merge (args)))]
      (alter-var-root #'app   (constantly (:gpt conf)))
      (alter-var-root #'build (constantly (:build conf)))
      (alter-var-root #'psql  (constantly (-> conf :gpt :psql)))
      conf))
;=
