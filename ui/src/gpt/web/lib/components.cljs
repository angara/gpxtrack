(ns gpt.web.lib.components
  (:require
    [taoensso.timbre    :refer  [debug]]
    ;[re-frame.core :as rf]
    ;
    [gpt.web.lib.icons :refer  [selector-arrow]]))
;=

(defn select [id classes val-options curr on-change]
  [:div.relative
    [:select.block.appearance-none.p-1.pl-3.pr-8.rounded
      { :id id
        :class classes
        :value curr
        :on-change #(on-change (-> % .-target .-value))}
      (for [[k v] val-options]
        ^{:key k} [:option {:value k} v])]
    (selector-arrow "text-gray-700")])
;;
