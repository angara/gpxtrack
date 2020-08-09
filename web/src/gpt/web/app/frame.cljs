(ns gpt.web.app.frame
  (:require
    [taoensso.timbre  :refer-macros [debug warn]]
    [re-frame.core    :as rf]
    ;
    [gpt.web.const  :refer [VIEW_DASHBOARD VIEW_DOCLIST]]
    ;
    [gpt.web.lib.icons       :as     icons]
    [gpt.web.views.dashboard :refer  [dashboard-view]]
    [gpt.web.views.doclist   :refer  [doclist-view]]))
;=


(def ^:const PANEL_LOGIN :panel/login)
(def ^:const PANEL_MAIN  :panel/main)

(def INIT_STATE
  { 
    :layout/active-view      VIEW_DASHBOARD})
    ; :navbar/burger-menu-open  false
    ; :navbar/user-menu-open    false})
;-

; (rf/reg-event-db            ;; usage:  (dispatch [:time-color-change 34562])
;   :navbar/burger-click       ;; dispatched when the user enters a new colour into the UI text field
;   (fn [db [_ _]]             ;; -db event handlers given 2 parameters:  current application state and event (a vector)
;     (update db :navbar/burger-menu-open not)))
; ;

; (rf/reg-event-db            
;   :navbar/user-menu-click    
;   (fn [db [_ _]]             
;     (println "user-menu-click")
;     (update db :navbar/user-menu-open not)))
; ;

; (rf/reg-sub
;   :navbar/burger-menu-open
;   (fn [db _]
;     (get db :navbar/burger-menu-open)))
; ;

; (rf/reg-sub
;   :navbar/user-menu-open
;   (fn [db _]
;     (get db :navbar/user-menu-open)))
; ;


(rf/reg-event-fx 
  ::set-active-view
  (fn [_ [_ view]]
    (debug "set-active-view:" view)
    (rf/dispatch [view])))
;-

(rf/reg-sub 
  :layout/active-view
  (fn [db _]
    ;; (debug "layout/active-view:" db)
    (get db :layout/active-view)))
;-

; (defn nav-bar []
;   [:nav.bg-indigo-600.p-2.m-0.w-full.fixed.z-10.pin-t
;     [:div.container.mx-auto.flex.flex-wrap.items-center
;       [:div.flex.w-full.justify-center.text-white.font-extrabold
;         [:a.text-white.no-underline
;           {:class "hover:text-white hover:no-underline" :href "#"}
;           [:span.text-2xl.pl-2
        
;         [:div.flex.w-full.pt-2.content-center.justify-between 
;           {:class "md:w-1/2 md:justify-end"}
;           [:ul.list-reset.flex.justify-between.flex-1.items-center
;             {:class "md:flex-none"}
;             [:li.mr-3
;               [:a.inline-block.py-2.px-4.text-white.no-underline {:href "#"} "Active"]]
;             [:li.mr-3
;               [:a.inline-block.py-2.px-4.text-white.no-underline {:href "#"} "Active 2"]]]]]]
  
;     ,]
;   ,)
; ;;

(def TOP_BUTTONS
  [{:text "Dashboard" :view VIEW_DASHBOARD}
   {:text "Документы" :view VIEW_DOCLIST}])
;-

(defn top-buttons []
  (let [active @(rf/subscribe [:layout/active-view])]
    [:ul.flex
      (for [btn TOP_BUTTONS
            :let [ {:keys [text view]} btn
                   active-cls (when (= view active) " text-yellow-200")]]
        ^{:key view}
        [:li.mx-4
         [:button.bg-blue-500.p-1.px-4.rounded
           {:class (str "hover:bg-blue-400" active-cls)
            :on-click #(rf/dispatch [::set-active-view view])}
          text]])]))
;;


(rf/reg-sub
  ::username
  (fn [db]
    (get-in db [:user :username])))
;;
 
(defn nav-bar []
  (let [username @(rf/subscribe [::username])]
    ;
    [:nav.flex.items-center.justify-between.flex-wrap.bg-indigo-600.p-3.px-6.inset-x-0.top-0.fixed
     {:style {:box-shadow "0 0 0.2rem rgba(0,0,0,.1), 0 0.2rem 0.4rem rgba(0,0,0,.2)"}}
     [:div.text-white.text-2xl.font-medium.px-1.tracking-wider 
        {:style {:text-shadow "0 1px 3px rgba(0,0,0,0.5)"}}
        "GPX Track"]
     [:div.flex-grow.text-white.text-xl.px-3
        [top-buttons]]
     [:div
      [:button.rounded.p-1.px-3.bg-indigo-400.text-white.flex
       {:class "hover:bg-indigo-300 hover:text-yellow"
        :on-click #(rf/dispatch [:user/login])}
       [:i.mr-2 (icons/person)]
       (or username "- login -")]]]))
;;


(defn icon [name]
  [:i.material-icons {:style {:margin-right "0.3rem"}} name])

;

(defn panel []
  (let [view* (rf/subscribe [:layout/active-view])]
        ;view @(rf/subscribe [:layout/active-view])]
    (fn []
      (debug "panel:" @view*)
      (condp = @view*
        VIEW_DASHBOARD  [dashboard-view]
        VIEW_DOCLIST    [doclist-view]
        (warn "unexpected view:" @view*)))))
;

(rf/reg-sub
  ::build
  (fn [db]
    (get db :build)))
;-

(defn frame []
  (let [build @(rf/subscribe [::build])]
    [:div.h-screen.bg-gray-200
      [nav-bar]
      [:div.mx-auto.px-4.h-full.p-3.pb-10.pt-20
        [:div.flex.flex-col.h-full.-mt-1
          [panel]]          
        [:footer.mt-4.bg-gray-300.p-1.bottom-0.inset-x-0.absolute.border-t.border-indigo-300
          [:div.mx-3.text-blue-600.text-sm.font-semibold.tracking-wider 
            (:appname build) " "
            [:span.text-gray-600 (:version build)]]]]]))
;;
