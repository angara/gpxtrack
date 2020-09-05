(ns gpt.main
  (:gen-class)
  (:require
    [taoensso.timbre    :refer  [debug info warn] :as timbre]
    [mount.core         :refer  [start-with-args]]
    ;;
    [mlib.config.core   :refer  [load-configs]]
    ; [mlib.thread        :refer  [join]]
    ;;
    [gpt.cfg            :refer  [build]]
    [gpt.app.server]))
;=

(defn -main []
  (info "init...")

  (timbre/merge-config!
    {:output-fn (partial timbre/default-output-fn {:stacktrace-fonts {}})})

  (let [mounted (start-with-args (load-configs))]
    (info "main:" (merge build mounted))))

  ; (info "exiting:" (join worker)))
;;
