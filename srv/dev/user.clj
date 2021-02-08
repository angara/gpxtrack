(ns user
  (:require
    [clojure.tools.namespace.repl :as tnr]
    ;[criterium.core :refer [quick-bench]]
    [mount.core :as mount]
    [hashp.core]
    ;    
    [gpt.main  :refer [-main]]
    [gpt.db.migration :as mg]))
;=

(-main)

(defn restart []
  (mount/stop)
  (mount/start))

(defn reset []
  (tnr/refresh :after 'user/restart))


(comment
  
;  (try (restart) (catch Exception _ _))

  (mount/start)
  (mount/stop)
  (restart)
  (reset)

  (mg/rollback)
  
  ,)

;;.
