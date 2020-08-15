(ns gpt.web.app.frame
  (:require
    [taoensso.timbre  :refer-macros [debug warn]]
    [re-frame.core    :as rf]
    ;
    [gpt.web.const  :refer [VIEW_DASHBOARD VIEW_DOCLIST]]
    ;
    [gpt.web.app.leaflet  :refer [map-panel]]
    [gpt.web.user.core    :refer  [user-bar]]
    ;
   ;
   ;
   ;;;;;
    [gpt.web.lib.icons       :as     icons]
    [gpt.web.views.dashboard :refer  [dashboard-view]]
    [gpt.web.views.doclist   :refer  [doclist-view]]))
;=

(defn gpxtrack-bar []
  [:div.text-2xl.text-center.p-1.font-semibold.tracking-wider.border-b
    [:div
      {:style {:filter "drop-shadow(1px 1px 0.7px rgba(0,0,0,0.5))"}}
      [:span.text-indigo-400 "GPX"] 
      [:span.text-indigo-700.mx-2 "Track"]]])
      ;[:span.text-indigo-400.inline-block (icons/location)]]])
;-

(def INIT_STATE {})
;;   { 
;;     :layout/active-view      VIEW_DASHBOARD})
;;     ; :navbar/burger-menu-open  false
;;     ; :navbar/user-menu-open    false})
;; ;-

(defn filters []
  [:div.p-2
    [:div "#tag1"]
    [:div "#tag2"]
    [:div "#tag3"]])
;

(defn track-list []
  [:div.h-full.relative
    [:div.bg-gray-300.overflow-y-auto.absolute.inset-0.rounded
      [:div.px-2 "track 1"]
      [:div.px-2 "track 2"]
      [:div.px-2 "track 3"]
      [:div.px-2 "track 1"]
      [:div.px-2 "track 2"]
      [:div.px-2 "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]
      [:div "track 1"]
      [:div "track 2"]
      [:div "track 3"]]])
;-

(rf/reg-sub
  ::build
  (fn [db]
    (get db :build)))
;-

;; (defn left-footer []
;;   (let [build @(rf/subscribe [::build])]
;;     [:div.px-1.text-sm.text-xs.tracking-wider.text-center
;;       [:span.text-blue-600 (:appname build) " "]
;;       [:span.text-gray-600 (:version build)]]))
;; ;-

(defn frame []
  [:div.h-screen.flex
    [:div.max-w-md.flex.flex-col
      { :class "w-1/4"
        :style {:min-width "20rem"}}
      [gpxtrack-bar]
      [filters]
      [:div.flex-grow.px-2.mb-1 
        [track-list]]]
      ;[:footer.flex-1.flex-grow-0 [left-footer]]]
    [:div.bg-green-300.flex-grow
      [map-panel]]
    [user-bar]])

;      [nav-bar]
      ;; [:div.mx-auto.px-4.h-full.p-3.pb-10.pt-20
      ;;   [:div.flex.flex-col.h-full.-mt-1
      ;;     [panel]]          
      ;;   [:footer.mt-4.bg-gray-300.p-1.bottom-0.inset-x-0.absolute.border-t.border-indigo-300
      ;;     [:div.mx-3.text-blue-600.text-sm.font-semibold.tracking-wider 
      ;;       (:appname build) " "
      ;;       [:span.text-gray-600 (:version build)]]]]]))
;;
