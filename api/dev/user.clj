(ns user
  (:require
    [clojure.tools.namespace.repl :as tnr]
    ;[criterium.core :refer [quick-bench]]
    [mount.core :refer [stop]]
    ;    
    [gpt.main  :refer [-main]]
    [gpt.db.migration :as mg]))
;=

(-main)

(defn start []
  (-main))

(defn restart []
  (stop)
  (start))

(defn reset []
  (tnr/refresh :after 'user/restart))


(comment
  
  (try (restart) (catch Exception _ _))

  (start)
  (stop)
  (reset)

  (mg/rollback)
  
  ,)

;;.
