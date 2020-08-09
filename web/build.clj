#!/bin/bash
#_(
    "exec" "bb" "--classpath" "tools" "$0" "$@"
    ,)

; - - - - - - - - - - - - - - - - - - -

(ns build.main
  (:require
;    [clojure.java.shell :refer [sh]]
;    [clojure.string :as str]
    [build-lib :refer [git-commit-hash cmd sh-c die iso-timestamp]]))
;=

(def DEV_RES  "./build/dev")
(def PROD_RES  "./build/prod")
  

(defn cssdev []
  (let [publ (str DEV_RES "/public/")]
    (cmd "mkdir" "-p" publ)
    (sh-c (str "cp -r assets/* " publ))
    (cmd "npx" "tailwindcss" "build" "assets/css/styles.css" 
                "-o" (str publ "css/styles.css"))))
;;

(defn prn-list [lst]
  (doseq [s lst]
    (println s)))

(defn main [args]
  (condp = (first args)
    "cssdev" (-> (cssdev) (prn-list))
    (die "Parametre required: cssdev")))
;;

;; - - - - - - - - - main - - - - - - - - - -
  
(main *command-line-args*)
  
;;.



; cssdev:
;         mkdir -p ${DEV_RES}/public/
;         cp -r assets/* ${DEV_RES}/public/
;         npx tailwindcss build assets/css/styles.css -o ${DEV_RES}/public/css/styles.css

; prod_assets:
;         @echo "prod assets"
;         @mkdir -p ${PROD_RES}/public
;         @cp -r assets/* ${PROD_RES}/public

; tailwindcss: export NODE_ENV = production
; tailwindcss:
;         @echo "build css"
;         @npx tailwindcss build assets/css/styles.css -o ${PROD_RES}/public/css/styles.css

; build_js:
;         @npx shadow-cljs release prod

; prodpack: prod_assets build_js tailwindcss
;         @echo "prod: assets, js, css."
