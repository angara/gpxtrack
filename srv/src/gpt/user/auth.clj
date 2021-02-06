(ns gpt.user.auth
  (:require
    [taoensso.timbre          :refer  [debug warn]]))
;=

(defn bind-messenger-code [body]
  (debug "bind-messenger-code:" body)
  ;; uid, msgr, ...
  ;; generate code
  ;; add user info to authcode
  {})
 ;;
