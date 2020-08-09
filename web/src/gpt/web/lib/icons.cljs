(ns gpt.web.lib.icons)

;; https://material.io/resources/icons/?icon=person&style=round
;; 
(def ^:const add-location-path
  (str
    "M13,7c0-0.55-0.44-1-1-1c-0.55,0-1,0.44-1,1v2H9c-0.55,0-1,0.44-1,1c0,0.55,0.44,1,1,1h2v2"
    " c0,0.55,0.44,1,1,1c0.55,0,1-0.44,1-1v-2h2c0.55,0,1-0.44,1-1c0-0.55-0.44-1-1-1h-2V7z" 
    " M12,2c4.2,0,8,3.22,8,8.2 c0,3.18-2.45,6.92-7.34,11.23c-0.38,0.33-0.95,0.33-1.33,0C6.45,17.12,4,13.38,4,10.2C4,5.22,7.8,2,12,2z"))
;-

(def ^:const person-path
  "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v1c0 .55.45 1 1 1h14c.55 0 1-.45 1-1v-1c0-2.66-5.33-4-8-4z")
;-

(def ^:const more-horiz-path
  "M6 10c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm12 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-6 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z")

(def ^:const assign-in-path
  (str 
    "M19 3h-4.18C14.4 1.84 13.3 1 12 1s-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0" 
    " 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zM9.29 16.29L6.7" 
    " 13.7c-.39-.39-.39-1.02 0-1.41.39-.39 1.02-.39 1.41 0L10 14.17l5.88-5.88c.39-.39 1.02-.39 1.41 0" 
    " .39.39.39 1.02 0 1.41l-6.59 6.59c-.38.39-1.02.39-1.41 0z"))
;-

(def ^:const location-path
  (str
    "M12 2C8.13 2 5 5.13 5 9c0 4.17 4.42 9.92 6.24 12.11.4.48 1.13.48 1.53 0C14.58 18.92 19 13.17 19 9c0-3.87-3.13-7-7-7zm0" 
    " 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"))
;-

(defn selector-arrow [classes]
  [:div.pointer-events-none.absolute.inset-y-0.right-0.flex.items-center.px-2
    {:class classes}
    [:svg.fill-current.h-4.w-4 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20"}
      [:path {:d "M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"}]]])
;=

(defn svg24 [& content]
  (apply vector
    :svg
    { :xmlns "http://www.w3.org/2000/svg" 
      :fill "currentColor"
      :height "24" :width "24" :viewBox "0 0 24 24"}
    content))
;;

(defn add-location []
  (svg24
    [:rect {:fill "none" :height "24" :width "24"}]
    [:g [:g [:path {:d add-location-path}]]]))
;;

(defn person []
  (svg24
    [:path {:fill "none" :d "M0 0h24v24H0V0z"}]
    [:path {:d person-path}]))
;;

(defn more-horiz []
  (svg24
    [:path {:fill "none" :d "M0 0h24v24H0V0z"}]
    [:path {:d more-horiz-path}]))
;;

(defn assign-in []
  (svg24
    [:path {:fill "none" :d "M0 0h24v24H0V0z"}]
    [:path {:d assign-in-path}]))
;;

(defn location []
  (svg24
    [:path {:fill "none" :d "M0 0h24v24H0V0z"}]
    [:path {:d location-path}]))
;;
