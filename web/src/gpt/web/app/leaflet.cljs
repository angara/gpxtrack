(ns gpt.web.app.leaflet
  (:require
   [taoensso.timbre  :refer-macros [debug]]
   [reagent.core     :refer [create-class]]
   [re-frame.core    :as rf]
   ["leaflet" :refer [Map TileLayer]]))
   ;["react-leaflet"  :refer [Map TileLayer ZoomControl Marker Popup]]))
    ;
  ;;  [gpt.web.const       :refer  [VIEW_DASHBOARD VIEW_DOCLIST]]
  ;;  [gpt.web.lib.icons   :as     icons]
  ;;  [gpt.web.lib.util    :refer-macros [evt-dispatch]]))
;=    

;; var map = new L.map("map-container",{ zoomControl: false});
;; L.control.zoom({ position: 'topright' }).addTo(map);

(defn- evt->latlon [evt]
  (when-let [coords (-> evt .-latlng)]
    { :lat (-> coords .-lat)
      :lon (-> coords .-lng)}))
;-

(defonce *map (atom nil))

(defn map-panel []

  (create-class
    { :display-name "LeafletMap"
      :component-did-mount
        (fn [_]
          (let [map   (reset! *map (Map. "leaflet-map"))
                layer (TileLayer. 
                        "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        #js { :attribution "&copy; OpenStreetMap"})]
            ;
            (.setView map #js [52.2752 104.2479] 9)
            (.addTo layer map)))

      :reagent-render
        (fn []
          [:div {:class "h-full" :id "leaflet-map"}])}))

  ; [:> Map
  ;   { :class "h-full"
  ;     :center {:lon 104.2479 :lat 52.2752} 
  ;     :zoom 9
  ;     ;:on-mouse-move (fn [evt] (debug (-> evt .-latlng)))
  ;     :on-click #(rf/dispatch [::map-click (evt->latlon %)])}
  ;   [:> TileLayer
  ;     {:attribution "&copy; OpenStreetMap"
  ;       :url "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"}]])
    ;[:> ZoomControl {:position "topright"}]])
    ;; (for [m markers :let [id (:id m)]]
    ;;   ^{:key id}
    ;;   [:> Marker 
    ;;     {:key id :position [(:lat m) (:lon m)]
    ;;       :on-click #(rf/dispatch [::marker-click id])}
    ;;     [:> Popup (:title m)]])])
;;

(rf/reg-event-db
  ::map-click
  (fn [db [_ latlon]]
    (debug "latlon:" latlon)
    (assoc db ::click-coords latlon)))

(rf/reg-sub
  ::click-coords
  (fn [db]
    (get db ::click-coords)))
;-

(defn- deg6 [lbl val]
  (when val
    [:span [:span.text-pink-700 str lbl] (.toFixed val 6)]))
;-

(defn coords-bar []
  (let [coords @(rf/subscribe [::click-coords])
        lat (deg6 "Lat: " (:lat coords))
        lon (deg6 "Lon: " (:lon coords))]
    [:div.absolute.text-sm.text-purple-800
      {:style 
        { :left "14px" :bottom "4px" :z-index 450}}
          ;:filter "drop-shadow(2px 2px 2px #ffffff)"}}
      lat [:i.mx-1 " "] lon]))
;;
