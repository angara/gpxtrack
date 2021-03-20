(ns gpt.cfg
  (:require
    [clojure.spec.alpha     :as       s]  
    [clojure.edn            :as       edn]
    [cprop.core             :refer    [load-config]]
    [cprop.source           :refer    [from-resource from-env]]
    [mount.core             :refer    [defstate args]]
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

(s/def ::apikey ::b/not-blank)

(s/def ::internal
  (s/keys
    :req-un [::apikey]))

; - - - - - - - - - - - - - - - - - - - 

(s/def ::bucket ::b/not-blank)

(s/def ::gpxtrack
  (s/keys
    :req-un [::psql ::jwt ::http ::internal ::bucket]))

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
(declare  internal)

(defstate config
  :start
    (let [conf (->> 
                  [ {:build (from-resource "build.edn")}
                    (when-let [cf (System/getenv "CONFIG_EDN")]
                      (-> cf slurp edn/read-string))
                    (from-env)
                    (args)]
                  (load-config :merge)
                  (b/conform! ::conf))]
      ;
      (alter-var-root #'app       (constantly (:gpxtrack conf)))
      (alter-var-root #'build     (constantly (:build conf)))
      (alter-var-root #'minio     (constantly (-> conf :minio)))
      (alter-var-root #'psql      (constantly (-> conf :gpxtrack :psql)))
      (alter-var-root #'internal  (constantly (-> conf :gpxtrack :internal)))
      ;
      conf))
;=
