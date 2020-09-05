(ns gpt.db.core
  (:require
    [clojure.string       :refer  [starts-with?]]
    [taoensso.timbre      :refer  [debug warn]]
    [mount.core           :refer  [defstate]]    
    ;
    [next.jdbc            :refer  [with-transaction execute!]]
    [next.jdbc.result-set :refer  [as-unqualified-maps]]  ;; as-unqualified-arrays
    [next.jdbc.date-time  :refer  [read-as-local]]
    ;
    [mlib.psql.conn       :refer  [pooled-datasource]]
    [mlib.psql.adapters]
    ;
    [gpt.cfg              :as     cfg]))
;= 

;; ;; ;; ;; ;; ;; ;; ;; ;; ;;

(defn- prepend-jdbc [s]
  (if-not (starts-with? s "jdbc:")
    (str "jdbc:" s)
    s))
;-

(def ^:dynamic *tx*)

(defstate ds
  :start
    (let [ds (pooled-datasource
                { :jdbcUrl      (-> cfg/psql :url prepend-jdbc)
                  :auto-commit  false})]        ;; for :fetch-size statement option)
      (read-as-local)
      (alter-var-root #'*tx* (constantly ds))
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
