(ns gpt.web.views.dashboard
  (:require
    [taoensso.timbre  :refer-macros [debug]]
    [re-frame.core    :as rf]
    ["react-leaflet"  :refer [Map TileLayer Marker Popup]]
    ;
    [gpt.web.const       :refer  [VIEW_DASHBOARD VIEW_DOCLIST]]    
    [gpt.web.lib.icons   :as     icons]
    [gpt.web.lib.util    :refer-macros [evt-dispatch]]))
;=    

(def INIT_STATE
  {
    :dashboard
    {
      :markers nil}})
      ;; [
      ;;   { :id 1 :doc-id "1" 
      ;;     :lat 52.1 :lon 104.5
      ;;     :title "Marker 1"}]}})
;=

(rf/reg-event-fx
  VIEW_DASHBOARD
  (fn [{db :db} _]
    ;; load data cofx
    (debug "set dashboard")
    {:db (assoc db :layout/active-view VIEW_DASHBOARD)}))
;=

(rf/reg-event-fx
  ::categ
  (fn [{db :db} [_ categ-id]]
    (debug "categ selected:" categ-id)
    { :db (assoc-in db [:doclist :filters :categ] categ-id)
      :dispatch [VIEW_DOCLIST]}))
;-

(rf/reg-event-fx
  ::show-on-map
  (fn [{db :db} [_ categ-id]]
    (debug "show-on-map:" categ-id)
    { :db       (assoc-in db [:dashboard :markers] nil)
      :api/call [:api/docs-find {:categ categ-id} ::docs-loaded-ok]}))
;-

(rf/reg-event-fx
  ::docs-loaded-ok
  (fn [{db :db} [_ resp]]
    (debug "docs-loaded:" resp)
    (let [markers
          (->> (:docs resp)
               (keep  (fn [{:keys [id title marker]}]
                        (when marker
                          { :id id 
                            :title title 
                            :lat (:lat marker) 
                            :lon (:lon marker)}))))]
      ;;
      {:db  (assoc-in db [:dashboard :markers] markers)})))
;-

(rf/reg-event-fx
  ::marker-click
  (fn [_ [_ id]]
    (debug "marker-click:" id)))
;-

(rf/reg-sub
  ::markers
  (fn [db]
    (-> db :dashboard :markers)))
;-

(defn dashboard-view []
  (let [markers @(rf/subscribe [::markers])]
    [:section.flex.h-full
      [:div.h-full.overflow-auto
        {:style {:min-width "20rem" :width "20rem"}}
        [:ul.p-1
          [:span.clearfix]]]
      [:div.bg-gray-300.h-full.rounded-lg.border.shadow.flex-grow.ml-3
        [:div.h-full
          [:> Map
            {:center {:lon 104.2479 :lat 52.2752} :zoom 9 :class "h-full"}
            [:> TileLayer
              {:attribution "&copy; OpenStreetMap"
                :url "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}]
            (for [m markers :let [id (:id m)]]
              ^{:key id}
              [:> Marker 
                {:key id :position [(:lat m) (:lon m)]
                  :on-click #(rf/dispatch [::marker-click id])}
                [:> Popup (:title m)]])]]]]))
;;

;; https://opentopomap.ru/#map=12/52.2752/104.2479
