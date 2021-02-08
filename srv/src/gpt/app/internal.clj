(ns gpt.app.internal)


(defn wrap-require-apikey [handler apikey]
  (let [authorization-header (str "apikey " apikey)]
    (fn [req]
      (prn "req.headers:" (:headers req))
      (if (= authorization-header (get-in req [:headers "authorization"])) 
        (handler req)
        {:status 403 :body "wrong apikey"}))))
;;

