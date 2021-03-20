(ns gpt.main
  (:gen-class)
  (:require
    [taoensso.timbre    :refer  [debug info warn] :as timbre]
    [mount.core         :refer  [defstate start stop]]
    ;;
    ; [mlib.thread        :refer  [join]]
    ;;
    [gpt.cfg            :refer  [build]]
    [gpt.db.migration   :refer  [migrate]]
    [gpt.app.server]))
;=

(defstate main
  :start
    (migrate))
;-

(defn -main []
  (info "init main")

  (timbre/merge-config!
    {:output-fn (partial timbre/default-output-fn {:stacktrace-fonts {}})
     :min-level [
                  [#{ "org.eclipse.jetty.*"
                      "org.graylog2.gelfclient.*"
                      "com.zaxxer.hikari.*"
                      "org.mongodb.driver.*"
                      "io.netty.*"} :info]
                  ;
                  [#{"*"} :debug]
                  ;
                  ,]})

  (try
    (let [mounted (start)]
      (info "main started:" (merge build mounted)))
      ; (info "exiting:" (join worker)))
    (catch Throwable ex
      (warn ex "main interrupted")
      (stop)))
  ,)
;;
