(ns gpt.db.core
  (:require
    [mount.core           :refer  [defstate]]
    [taoensso.timbre      :refer  [debug warn]]
    ;
    [next.jdbc            :refer  [with-transaction execute!]]
    [next.jdbc.result-set :refer  [as-unqualified-maps]]  ;; as-unqualified-arrays
    [next.jdbc.date-time  :refer  [read-as-local]]
    [migratus.core        :as     migratus]
    ;
    [mlib.psql.conn       :refer  [pooled-datasource]]
    [mlib.psql.adapters]
    ;
    [gtp.cfg              :as     cfg]))
;= 

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(def ^:dynamic *tx*)

(defstate ds
  :start
    (let [ds (pooled-datasource
                { :jdbcUrl      (:url cfg/psql)
                  :auto-commit  false})        ;; for :fetch-size statement option)
          mcf (:db {:connection ds})]
      (alter-var-root #'*tx* (constantly ds))
      (read-as-local)
      (when (:init-db cfg/psql)
        (debug "init-db")
        (migratus/init mcf))
      ;
      (when (:migrate cfg/psql)
        (debug "run migrations")
        (migratus/migrate mcf))
      ds)
  :stop
    (do
      (alter-var-root #'*tx* (constantly nil))
      (.close ds)))
;-

(defmacro transact [& body]
  `(with-transaction [tx# ds]
     (binding [*tx* tx#]
       ~@body)))
;-

(def ^:const EXEC_OPTS
  {:builder-fn as-unqualified-maps :return-keys true})
;;

(defn exec! [stmt]
  (execute! *tx* stmt EXEC_OPTS))

;; ;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(def ^:const LIMIT_DEFAULT 10000)

(def ^:const USER       :user)
(def ^:const USER_AUTH  :user_auth)

;; ;; ;; ;; ;; ;; ;; ;; ;; ;; ;;
