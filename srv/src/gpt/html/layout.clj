(ns gpt.html.layout
  (:require
    [hiccup.page :refer [html5]]))
;    [mlib.http.util :refer [make-url]]))
;=

(def SITE_NAME    "gpxtrack.info")
(def SITE_TITLE   "GPX Track")
(def SITE_DESCR   "GPX Track: треки по активным турам, веломарштуры, бег, скитур, лыжные походы")
(def FAVICON      "https://gpxtrack.info/img/favicon.ico")
(def OG_IMAGE     "https://gpxtrack.info/img/og-image.png")


(defn head-meta
  [{:keys [title page-title descr og-title og-image og-url og-descr]}]
  (let [wt (or title page-title)
        ds (or descr SITE_DESCR)
        og-tit (or og-title wt SITE_NAME)
        og-img (or og-image OG_IMAGE)]
    (list
      [:title SITE_TITLE (and wt (str ": " wt))]
      [:link {:rel "shortcut icon" :href FAVICON}]
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width,initial-scale=1"}]
      "\n"
      [:meta {:name "description" :content ds}]
      [:meta {:property "og:site_name" :content SITE_NAME}]
      [:meta {:property "og:type"   :content "article"}]
      [:meta {:property "og:locale" :content "ru_RU"}]
      [:meta {:property "og:image"  :content og-img}]
      [:meta {:property "og:title"  :content og-tit}]
      [:meta {:property "og:description" :content (or og-descr ds)}]
      (when og-url 
        [:meta {:property "og:url" :content og-url}]))))
;;

(defn html-resp [content]
  {:status 200
   :headers {"Content-Type" "text/html;charset=utf-8"}
   :body (html5 content)})
;;

(defn head-body [{:keys [extra] :as head} body]
    (list
      [:head 
        (concat 
          (head-meta head)
          extra)]
      "\n"
      [:body
        body]))
;;

(comment

  (html-resp [:div {:class "a"}])
  ;; => {:body "<!DOCTYPE html>\n<html><div class=\"a\"></div></html>",
  ;;     :headers {"Content-Type" "text/html;charset=utf-8"},
  ;;     :status 200}

  (->
    (head-body
      {:title "title" :extra [:script "qwe"]}
      [:div.body "page body"])
    (html-resp))

  ,)
