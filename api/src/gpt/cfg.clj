(ns gpt.cfg
  (:require
    [clojure.spec.alpha     :as       s]
    [mount.core             :refer    [defstate args]]
    [medley.core            :refer    [deep-merge]]
    [ring.middleware.token  :as       token]
    [gpt.spec.base          :as       b]))
;=

; - - - - - - - - - - - - - - - - - - - 

(s/def  :cfg-http/max-body pos?)

(s/def  ::http
  (s/keys
    :req-in [::b/host ::b/port]))

; - - - - - - - - - - - - - - - - - - - 

(s/def ::init-db boolean?)
(s/def ::migrate boolean?)

(s/def  ::psql
  (s/keys 
    :req-un [::b/url]
    :opt-un [::init-db ::migrate]))

; - - - - - - - - - - - - - - - - - - - 
;
(s/def :jwt-config/exp  pos?)

(s/def ::jwt-opts 
  (s/keys 
    :req-un [::token/alg ::token/leeway-seconds ::token/secret :jwt-config/exp]))

(s/def ::issuers
  (s/map-of ::b/not-blank ::jwt-opts))

(s/def ::issuer ::b/not-blank)

(s/def ::jwt
  (s/keys :req-un [::issuer ::issuers]))

; - - - - - - - - - - - - - - - - - - - 

(s/def ::bucket ::b/not-blank)

(s/def ::gpxtrack
  (s/keys
    :req-un [::psql ::jwt ::http ::bucket]))

; - - - - - - - - - - - - - - - - - - - 

(s/def ::access-key ::b/not-blank)
(s/def ::secret-key ::b/not-blank)

(s/def ::minio
  (s/keys
    :req-un [::b/url ::access-key ::secret-key]))

(s/def  ::conf
  (s/keys
    :req-un [::gpxtrack ::build ::minio]))

; - - - - - - - - - - - - - - - - - - - 

(declare  app)
(declare  build)
(declare  psql)
(declare  minio)

(defstate conf
  :start
    (let [conf (b/conform! ::conf (apply deep-merge (args)))]
      (alter-var-root #'app   (constantly (:gpxtrack conf)))
      (alter-var-root #'build (constantly (:build conf)))
      (alter-var-root #'minio (constantly (-> conf :minio)))
      (alter-var-root #'psql  (constantly (-> conf :gpxtrack :psql)))
      conf))
;=
