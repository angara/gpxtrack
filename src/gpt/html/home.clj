(ns gpt.html.home
  (:require
    [gpt.html.layout  :refer [html-resp head-body]]))
;=


(defn h-root-page [_]
  (->
    (head-body {} 
      (list
        [:h1 "GPX Track"]
        [:a {:href "https://angara.net"} "Angara.Net"]))
    (html-resp)))
;;

(defn h-not-found [_]
  (prn "not-found")
  (->
    (head-body 
      {}
      [:h2 "Not Found"])
    (html-resp)))
;;
