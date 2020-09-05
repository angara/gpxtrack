(ns bmk.pom
  (:require
    [clojure.data.xml :as xml]))
;=

(defn make-pom [group artifact version descr url]
  (->
    [:project {:xmlns "http://maven.apache.org/POM/4.0.0"}
      [:modelVersion {} "4.0.0"]
      [:groupId {} group]
      [:artifactId {} artifact]
      [:version {} version]
      [:description {} descr]
      [:url {} url]
      [:scm {} 
        [:url {} url]]
      [:properties {}
        [:maven.compiler.source {} "11"]
        [:maven.compiler.target {} "11"]]
      [:build {}
        [:sourceDirectory {} "src"]]]
    ;
    (xml/sexp-as-element)
    (xml/emit-str)))
;;
