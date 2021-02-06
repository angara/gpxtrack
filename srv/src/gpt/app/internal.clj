(ns gpt.app.internal)


(defn wrap-require-apikey [handler apikey]
  (let [authorization-header (str "apikey " apikey)]
    (fn [req]
      (if (= authorization-header (get-in req [:headers "Authorization"])) 
        (handler req)
        {:status 403 :body "wrong apikey"}))))
;;

