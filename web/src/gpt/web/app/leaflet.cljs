(ns gpt.web.app.leaflet
  (:require
   [taoensso.timbre  :refer-macros [debug]]
   [re-frame.core    :as rf]
   ["react-leaflet"  :refer [Map TileLayer ZoomControl Marker Popup]]))
    ;
  ;;  [gpt.web.const       :refer  [VIEW_DASHBOARD VIEW_DOCLIST]]
  ;;  [gpt.web.lib.icons   :as     icons]
  ;;  [gpt.web.lib.util    :refer-macros [evt-dispatch]]))
;=    

;; var map = new L.map("map-container",{ zoomControl: false});
;; L.control.zoom({ position: 'topright' }).addTo(map);

(defn map-panel []
  [:> Map
    {:center {:lon 104.2479 :lat 52.2752} :zoom 9 :class "h-full"}
    [:> TileLayer
      {:attribution "&copy; OpenStreetMap"
        :url "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}]])
    ;[:> ZoomControl {:position "topright"}]])
    ;; (for [m markers :let [id (:id m)]]
    ;;   ^{:key id}
    ;;   [:> Marker 
    ;;     {:key id :position [(:lat m) (:lon m)]
    ;;       :on-click #(rf/dispatch [::marker-click id])}
    ;;     [:> Popup (:title m)]])])
;;
