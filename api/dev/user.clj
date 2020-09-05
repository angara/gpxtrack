(ns user
  (:require
    [clojure.tools.namespace.repl :as tnr]
    ;[criterium.core :refer [quick-bench]]
    [mount.core :refer [stop]]
    ;    
    [gpt.main  :refer [-main]]))
;=

(-main)

(defn start []
  (-main))

(defn reset []
  (tnr/refresh :after 'restart))

(defn restart []
  (stop)
  (start))

(comment
  
  (try (restart) (catch Exception _ _))

  (start)
  (stop)
  (reset)

  ,)

;;.
