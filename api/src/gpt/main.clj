(ns gpt.main
  (:gen-class)
  (:require
    [mount.core         :refer  [start-with-args]]
    [taoensso.timbre    :refer  [debug info warn] :as timbre]
    ;;
    [mlib.config.core   :refer  [load-configs]]
    ; [mlib.thread        :refer  [join]]
    ;;
    [oncab.cfg        :refer  [build]]
    [oncab.app.server]))
;=

;; XXX: run migrations

(defn -main []
  (info "init...")

  (timbre/merge-config!
    {:output-fn (partial timbre/default-output-fn {:stacktrace-fonts {}})})

  (let [mounted (start-with-args (load-configs))]
    (info "main:" (merge build mounted))))

  ; (info "exiting:" (join worker)))
;;
