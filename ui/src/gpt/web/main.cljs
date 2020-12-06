(ns gpt.web.main
  (:require 
    [reagent.dom  :as rdom]
    [re-frame.core :as rf]
    [gpt.web.app.frame :refer  [frame]]
    [gpt.web.state :refer [init-state]]))
;=

;; (enable-console-print!)

(defn app []
  [frame]) 
;;
 
(defn render [app]
  (-> js/document
      (.-title)
      (set! "GPX Track"))
  ;
  (js/document.addEventListener "keyup"
     #(when (= 27 (-> % .-keyCode))
        (rf/dispatch [:close-modal])))
  ;
  (rdom/render [app]
    (js/document.getElementById "app_root")))
;;

(defn main! []
  (init-state)
  (rf/dispatch-sync [:initialize])
  (rf/dispatch [:system-info])
  (render app))
;;

(defn reload! []
  (println "reload!")
  (rf/clear-subscription-cache!)
  (render app))
;;
