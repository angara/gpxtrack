(ns gpt.web.lib.modal
  (:require
    [taoensso.timbre    :refer  [debug]]
    [re-frame.core :as rf]
    ;
    [gpt.web.lib.icons :as icons]
    [gpt.web.lib.btn    :refer [btn-prim btn-white btn-warn]]))
;=


(rf/reg-event-db
  :show-modal
  (fn [db [_ modal]]
    (assoc db ::modal modal)))
;

(rf/reg-event-db
  :close-modal
  (fn [db _]
    (dissoc db ::modal)))
;

(rf/reg-sub
  ::modal
  (fn [db]
    (::modal db)))
;
  
;;  overflowX: 'hidden',
;;  overflowY: 'auto',)
    
(defn evt-stop [evt]
  (.preventDefault evt)
  (.stopPropagation evt)
  nil)
;;

(defn close [evt]
  (evt-stop evt)
  (rf/dispatch [:close-modal]))
;;

(defn modal-panel []
  (let [modal @(rf/subscribe [::modal])]
    (when modal
      [:div.flex.items-center.justify-center.fixed.inset-0.p-4.opacity-100.overflow-hidden
        { :style {:z-index 1000 :background-color "rgba(0,0,0,0.3)" :animation "show .5s ease"}
          :on-click  close
          :on-key-press (fn [evt] (debug "keyup:" evt))
          :on-key-down  (fn [evt] (debug "key-down:" evt))}
       
        [:div.opacity-100.bg-white.rounded-lg.px-4.py-2.shadow-lg.flex-col.relative
          { :on-click evt-stop
            :style 
            {
              :min-width "320px"}}
          [:div.flex-1
            "header"]
          [:div.flex-grow.py-2
           "content content content"]
          [:dov.flex-1.flex
            (btn-prim
              {}
              #(debug "click:" %)
              "Ok")
            (btn-white
              { :style {:margin-left "1rem"}}
              close
              "Cancel")]
          ;
          [:botton.absolute.cursor-pointer.text-gray-500
            { :on-click close
              :class "hover:text-gray-800"
              :style {:top "5px" :right "6px"}}
            (icons/close)]]
            ;(update (icons/close) 1 merge {:width 24 :height 24})]]
        
            ;; :on-click (fn [evt] 
            ;;             (.stopPropagation evt)
            ;;             (debug "click-1:" evt))
            ;; :on-key-press (fn [evt] (debug "keypress-1:" evt))
            ;; :on-key-down  (fn [evt] (debug "key-down-1:" evt))}


        [:botton.absolute.cursor-pointer.text-white
          { :on-click close
            :style {:top "10px" :right "10px" :filter "drop-shadow(1px 1px 1px rgba(0,0,0,0.6))"}}
          (update (icons/close) 1 merge {:width 40 :height 40})]])))



        
;;
