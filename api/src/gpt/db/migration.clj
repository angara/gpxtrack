(ns gpt.db.migration
  (:require
    [taoensso.timbre      :refer  [debug warn]]
    [migratus.core        :as     migratus]
    ;
    [gpt.cfg              :as     cfg];
    [gpt.db.core          :refer  [ds]]))
;=

(defn get-config []
  {:db                   {:datasource ds}
   :store                :database
   :migration-dir        "migrations/"
   :migration-table-name "migrations"
   :init-script          "init.sql"})
;-

(defn migrate []
  (debug "migrate")
  (when (:init-db cfg/psql)
    (debug "init-db")
    (migratus/init (get-config)))
  ;
  (migratus/migrate (get-config))
  ;
  true)
;;

(defn create [name & [edn?]]
  (if edn?
    (migratus/create (get-config) name :edn)
    (migratus/create (get-config) name)))
;;

(defn rollback []
  (migratus/rollback (get-config)))
;;
