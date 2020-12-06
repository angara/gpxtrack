(ns gpt.web.views.doclist
  (:require
    [taoensso.timbre  :refer-macros [debug]]
    [re-frame.core    :as rf]
    ;
    [gpt.web.const           :refer  [VIEW_DOCLIST]]
    [gpt.web.lib.icons       :as     icons]
    [gpt.web.lib.components  :refer  [select]]))
;=

(rf/reg-event-fx
  VIEW_DOCLIST
  (fn [{db :db} _]
    (let [filters (-> db :doclist :filters)]
      (debug "set doclist:" db)
      { :db       (assoc db :layout/active-view VIEW_DOCLIST)
        :api/call [:api/docs-find {:categ (:categ filters)} ::docs-loaded]})))
;=

(rf/reg-event-fx
  ::docs-loaded
  (fn [{db :db} [_ resp]]
    (debug "::docs-loaded:" resp)
    {:db (assoc-in db [:doclist :docs] (:docs resp))}))
;-

(rf/reg-sub
  ::docs
  (fn [db]
    (get-in db [:doclist :docs])))
;;

(rf/reg-event-fx
  ::filters-categ
  (fn [{db :db} [_ categ-id]]
    {:db      (assoc-in db [:doclist :filters :categ] categ-id)
     :dispatch [VIEW_DOCLIST]}))
;;

(rf/reg-sub
  ::filters-categ
  (fn [db]
    (get-in db [:doclist :filters :categ])))
;-

(rf/reg-event-fx
  ::docs-create
  (fn [{db :db} _]
    (let [_init-fields 
          { :categ    (-> db :doclist :filters :categ)
            :is-new?  true}]
      ;{ :dispatch [VIEW_DOCEDIT nil init-fields]})))
      {})))
;-

(defn filters []
  (let [curr-categ @(rf/subscribe [::filters-categ])]
    [:div
      [:label {:for "fliters-categ"} "Категории"]
      (select
        "fliters-categ"
        "max-w-lg"
        []
        ; (concat [["" ""]]
        ;   (map 
        ;     #(vector (:id %) (:title %))
        ;     CATEGS))
        curr-categ
        #(rf/dispatch [::filters-categ %]))]))
;-

(defn doclist-view []
  (let [docs @(rf/subscribe [::docs])]
    [:section
      [:h1.border-b.border-indigo-400.text-3xl "Список документов"]
      [:div.flex.my-4.justify-between
        [filters]
        [:div
          [:button.rounded.p-1.pl-2.pr-3.bg-green-500.text-white.shadow.flex
            { :class "hover:bg-green-400 hover:text-yellow"
              :on-click #(rf/dispatch [::docs-create])}
            ;
            [:i.mr-2 (icons/add-location)] "Добавить"]]]

      [:table.w-full.tbl-spaced
        [:thead
          [:tr
            [:th.p-2 {:class "w-3/4"} "Название"]
            [:th.p-2 "Дата"]
            [:th]]]
        [:tbody
          (for [{:keys [id title ct]} docs]
                ;:let [ doc]]
            ^{:key id}
            [:tr.border-b.border-gray-400
              {:class "hover:bg-gray-400"}
              [:td title]
              [:td.text-center ct]
              [:td 
                [:button.mt-1.text-blue-600.rounded.px-1
                  { :class "hover:bg-gray-200" 
                     ;; hover:border-2 hover:border-blue-700"
                     :on-click #(rf/dispatch [::XXX id])} 
                  (icons/more-horiz)]]])]]]))
;;
