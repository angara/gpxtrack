(ns gpt.web.lib.btn
  (:require
   [taoensso.timbre    :refer  [debug]]
   [re-frame.core :as rf]
    ;
   [gpt.web.lib.icons :as icons]))
;=


(defn evt-stop [evt]
  (.preventDefault evt)
  (.stopPropagation evt)
  evt)
;;


(defn btn-prim [attr on-click text]
  [:button.flex.px-5.py-1.bg-green-500.rounded-lg.shadow.text-white.outline-none.font-semibold
    (merge
      { :class "hover:bg-green-600 active:border-none focus:shadow-outline"
        :on-click (fn [evt] (evt-stop evt) (on-click evt) nil)}
      attr)
    text])
;;
