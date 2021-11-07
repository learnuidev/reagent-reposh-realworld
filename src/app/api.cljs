(ns app.api)

(defonce api-uri "https://conduit.productionready.io/api")

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn get-auth-header []
  (let [token (.getItem js/localStorage "auth-user-token")]
    [:Authorization (str "Token " token)]))
