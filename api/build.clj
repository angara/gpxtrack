#!/bin/bash
#_(
    "exec" "bb" "--classpath" "tools" "$0" "$@"
    ,)
   
  
(ns build.main
  (:require
    [clojure.java.shell :refer [sh]]
    [clojure.string :as str]
    [build-lib :refer [git-commit-hash cmd iso-timestamp]]))
;    [a :refer [b]]))

;; (load-file "./tools/build-lib.clj")
;; (sh "ls")

;; (prn (git-commit-hash))


;; - - - - - - - - - main - - - - - - - - - -
  
(prn *command-line-args*)
  
; (-> (sh "ls" "-l") :out (str/split #"\n"))

;(prn b)

(println (git-commit-hash))
(println (iso-timestamp))

;; (cmd "curl" "-l")

;;.
