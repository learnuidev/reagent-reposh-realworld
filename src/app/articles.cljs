(ns app.articles
  (:require [reagent.core :as r]
            [app.api :refer [api-uri]]
            [ajax.core :refer [GET json-response-format]]))

(defonce articles-state (r/atom nil))

(comment
  @articles-state)
(defn handler [response]
  (reset! articles-state response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn articles-browse []
  (GET (str api-uri "/articles?limit=20")
       {:handler handler
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (articles-browse)
  (first (:articles (deref articles-state))))
