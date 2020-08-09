(ns gpt.web.lib.util)

(defmacro hevent
  ([& body]
   `(fn [~'evt] (.preventDefault ~'evt) ~@body nil)))
;;

(defmacro evt-dispatch [& arglist]
  `(fn [~'evt] 
    (.preventDefault ~'evt)
    (.stopPropagation ~'evt)
    (re-frame.core/dispatch [~@arglist])))
;;

(comment
  
  (macroexpand `(evt-dispatch :a :b))
  
  ,)
