(ns gpt.web.state
  (:require
    [taoensso.timbre      :refer  [debug warn]]
    [re-frame.core        :as   rf]
    [gpt.web.lib.api      :as   api]
    [gpt.web.app.frame    :as   frame]
    [gpt.web.user.core    :as   user]
    [gpt.web.views.dashboard :as dashboard]))
;=

(rf/reg-event-fx
  ::system-info-ok
  (fn [{db :db} [_ {:keys [build]}]]
    (debug "system-info:" build)
    {:db (assoc db :build build)}))
;-

(rf/reg-event-fx
  :system-info
  (fn [_ _]
    {:api/call [:api/system-info {} ::system-info-ok]}))

(defn init-state []
  (rf/reg-event-db
    :initialize
    (fn [_ _]
      (merge
        api/INIT_STATE
        user/INIT_STATE
        frame/INIT_STATE
        dashboard/INIT_STATE
        {:doc {}}           ;; :id "123"
          ;; {:categ "..." :is-new? true}
        ;; XXX
        { :doclist 
          { :docs []
            :filter {}}}))))    ;; :categ

        ;; :...docedit/allow-save true
;-



;; interceptor {:id ... :before (fn [context] ...) :after (fn [context] ...)}
;; 
;; context:
;; 
;; {:coeffects {:event [:some-id :some-param]
;;              :db    <original contents of app-db>}

;;  :effects   {:db    <new value for app-db>
;;              :dispatch  [:an-event-id :param1]}

;;  :queue     <a collection of further interceptors>
;;  :stack     <a collection of interceptors already walked>}
;;  
 
 
;; (def trim-event
;;   (re-frame.core/->interceptor
;;    :id      :trim-event
;;    :before  (fn [context]
;;               (let [trim-fn (fn [event] (-> event rest vec))]
;;                 (update-in context [:coeffects :event] trim-fn)))))
;;                 
;;                 
;; (reg-fx
;;   :db
;;   (fn [value]
;;     (reset! re-frame.db/app-db value)))
;;     
;; (reg-event-fx
;;   :some-id
;;   [(inject-cofx :random-int 10) (inject-cofx :now)  (inject-cofx :local-store "blah")]  ;; 3
;;   (fn [cofx _]
;;     ;; ... in here I can access cofx's keys :now :local-store and :random-int))
;;   ))

;; (reg-cofx               ;; registration function
;;   :now                 ;; what cofx-id are we registering
;;   (fn [coeffects _]    ;; second parameter not used in this case
;;      (assoc coeffects :now (js.Date.))))   ;; add :now key, with value  

;; (reg-cofx               ;; new registration function
;;   :local-store
;;   (fn [coeffects local-store-key]
;;     (assoc coeffects
;;           :local-store
;;           (js->clj (.getItem js/localStorage local-store-key)))))

;; (reg-sub
;;   :id
;;
;;   ;; signals function
;;   (fn [_ _]
;;     [(subscribe [:a]) (subscribe [:b 2])])     ;; <-- these are the inputs
;;
;;   ;; computation function
;;   (fn [[a b] _]                   ;; input values supplied in a vector
;;     (calulate-it a b)))

;; (reg-sub
;;   :id
;;
;;   ;; input signals 
;;   :<- [:a]        ;; means (subscribe [:a] is an input)
;;   :<- [:b 2]      ;; means (subscribe [:b 2] is an input)
;;
;;   ;; computation function
;;   (fn [[a b] _]
;;     (calulate-it a b)))


;; (defn clock
;;   []
;;   [:div.example-clock
;;    {:style {:color @(rf/subscribe [:time-color])}}
;;    @(rf/subscribe [:time-str])])

;; (reg-sub 
;;   :time-str 
;;
;;   ;; signals function 
;;   (fn [_ _]  
;;     (subscribe [:time]))
;;
;;   ;; computation function
;;   (fn [t _] 
;;     (-> t
;;        .toTimeString
;;        (clojure.string/split " ")
;;        first)))
;;        
;; (def <sub (comp deref re-frame.core/subscribe))   ;; same as `listen` (above)
;; (def >evt re-frame.core/dispatch)
;; 
;; src
;; ├── core.cljs             <--- entry point, plus history, routing, etc
;; ├── panel-1
;; │   ├── db.cljs           <--- schema, validation, etc  (data layer)
;; │   ├── subs.cljs         <--- subscription handlers  (query layer)
;; │   ├── views.cljs        <--- reagent components (view layer)
;; │   └── events.cljs       <--- event handlers (control/update layer)
;; ├── panel-2
;; │   ├── db.cljs           <--- schema, validation. etc  (data layer)
;; │   ├── subs.cljs         <--- subscription handlers  (query layer)
;; │   ├── views.cljs        <--- reagent components (view layer)
;; │   └── events.cljs       <--- event handlers (control/update layer)
