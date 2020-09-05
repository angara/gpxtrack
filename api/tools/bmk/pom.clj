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


        ;; (xml/element :project {:xmlns "http://maven.apache.org/POM/4.0.0"}
        ;;   (xml/element :modelVersion {} "4.0.0")
        ;;   (xml/element :groupId {} group)
        ;;   (xml/element :artefactId {} artefact)
        ;;   (xml/element :version {} version)
        ;;   (xml/element :description {} descr)
        ;;   (xml/element :url {} url)
        ;;   (xml/element :scm {}
        ;;     (xml/element :url {} url))
        ;;   (xml/element :properties {}
        ;;     (xml/element :maven.compiler.source {} "11")
        ;;     (xml/element :maven.compiler.target {} "11"))
        ;;   (xml/element :build {}
        ;;     (xml/element :sourceDirectory {} "src")))]
