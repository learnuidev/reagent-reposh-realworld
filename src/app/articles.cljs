(ns app.articles
  (:require [reagent.core :as r]
            [app.api :refer [api-uri get-auth-header]]
            [ajax.core :refer [GET json-response-format]]))

(defonce articles-state (r/atom nil))
(defonce loading-state (r/atom false))

(comment
  @articles-state)
(defn handler [response]
  (reset! loading-state false)
  (reset! articles-state response))

(defn error-handler [{:keys [status status-text]}]
  (reset! loading-state false)
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn articles-browse []
  (reset! loading-state true)
  (GET (str api-uri "/articles?limit=20")
       {:handler handler
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (articles-browse)
  (first (:articles (deref articles-state))))

;; const limit = (count, p) => `limit=${count}&offset=${p ? p * count : 0}`;
(defn limit [total page]
  (str "limit=" total "&offset=" (or (* page total) 0)))
;;
(comment
  (limit 5 0))
(defn articles-feed []
  (reset! loading-state true)
  (GET (str api-uri "/articles/feed?limit=10&offset=0")
       {:handler handler
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

;;
(defn fetch-by [author page]
  (reset! loading-state true)
  (GET (str api-uri "/articles?author=" (js/encodeURIComponent author) "&" (limit 5 page))
       {:handler handler
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (fetch-by "learnuidev2@gmail.com" 0))

(defn favourited-by [author page]
  (reset! loading-state true)
  (GET (str api-uri "/articles?favorited=" (js/encodeURIComponent author) "&" (limit 5 page))
       {:handler handler
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (favourited-by "learnuidev2@gmail.com" 0))
