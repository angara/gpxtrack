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


(defn btn-prim [attr on-click content]
  [:button.btn.flex.px-5.bg-green-500.rounded-md.text-white.font-semibold
    (merge
      { :class "hover:bg-green-600"
        :on-click (comp on-click evt-stop)}
      attr)
    content])
;;

(defn btn-info [attr on-click content]
  [:button.btn.flex.px-5.bg-blue-500.rounded-md.text-white.font-semibold
    (merge
      { :class "hover:bg-blue-600"
        :on-click (comp on-click evt-stop)}
      attr)
   content])
;;

(defn btn-white [attr on-click content]
  [:button.btn.flex.px-5.bg-white.rounded-md.text-gray-700.font-semibold.border.border-gray-400
    (merge
      { :class "hover:bg-gray-300"
        :on-click (comp on-click evt-stop)}
      attr)
    content])
;;

(defn btn-warn [attr on-click content]
  [:button.btn.flex.px-5.bg-red-500.rounded-md.text-black.font-semibold
    (merge
      { :class "hover:bg-red-600"
        :on-click (comp on-click evt-stop)}
      attr)
   content])
;;
