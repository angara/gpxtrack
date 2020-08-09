(ns build-lib
  (:import
    [java.time Instant ZonedDateTime]
    [java.time.format DateTimeFormatter])
  (:require
    [clojure.string     :refer  [split]]
    [clojure.java.shell :refer  [sh]]))

;=

(defn cmd [& cmd-args]
  (let [{:keys [exit out]} (apply sh cmd-args)]
    (if (= 0 exit)
      (split out #"\n")
      (throw (ex-info (str "cmd failed: " (first cmd-args) ) {:exit exit})))))
;;

(defn git-commit-hash []
  (first (cmd "git" "rev-parse" "HEAD")))
  ;; (-> (sh "git" "rev-parse" "HEAD")
  ;;   :out
  ;;   (split #"\n")
  ;;   first))
;;

(defn iso-timestamp []
  (.format (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ssX") (ZonedDateTime/now)))
;;

;; (defn iso-timestamp []
;;   (LocalDateTime/now))

;; (def now (java.time.ZonedDateTime/now))
;; (def LA-timezone (java.time.ZoneId/of "America/Los_Angeles"))
;; (def LA-time (.withZoneSameInstant now LA-timezone))
;; (def pattern (java.time.format.DateTimeFormatter/ofPattern "HH:mm"))
;; (println (.format LA-time pattern))
