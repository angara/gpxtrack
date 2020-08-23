(ns bmk.core
  (:import
    [java.time ZonedDateTime]
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

(defn sh-c [args]
  (let [{:keys [exit out]} (sh "sh" "-c" args)]
    (if (= 0 exit)
      (split out #"\n")
      (throw (ex-info (str "sh-c failed: " args) {:exit exit})))))
;;

(defn die [msg]
  (println msg)
  (System/exit 1))
;;

(defn print-lines [lines]
  (cond
    (seq lines )        (doseq [l lines] (println l))    
    (not (nil? lines))  (println lines)
    :else               nil))
;;

(defn git-commit-hash []
  (first (cmd "git" "rev-parse" "HEAD")))
;;

(defn iso-timestamp []
  (.format (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ssX") (ZonedDateTime/now)))
;;
