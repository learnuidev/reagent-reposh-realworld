(ns app.articles
  (:require [reagent.core :as r]
            [app.api :refer [api-uri get-auth-header]]
            [ajax.core :refer [GET POST PUT json-request-format json-response-format]]))

(defonce articles-state (r/atom nil))
(defonce current-article-state (r/atom nil))
(defonce tab-state (r/atom :all))
(defonce tag-state (r/atom nil))
(defonce loading-state (r/atom false))
(defonce submitting-state (r/atom false))
(defonce error-state (r/atom nil))

(comment
  @current-article-state)
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
  (GET (str api-uri "/articles?limit=10&offset=0")
       {:handler handler
        :headers (get-auth-header)
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
(defn fetch-by
  ([author] (fetch-by author 0))
  ([author page]
   (reset! loading-state true)
   (GET (str api-uri "/articles?author=" (js/encodeURIComponent author) "&" (limit 5 page))
        {:handler handler
         :headers (get-auth-header)
         :response-format (json-response-format {:keywords? true})
         :error-handler error-handler})))

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

;;
(defn articles-by-tag [tag]
  (reset! loading-state true)
  (GET (str api-uri "/articles?" (limit 10 0) "&tag=" tag)
       {:handler handler
        :error-handler error-handler
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})}))

(comment
  (articles-by-tag @tag-state))

;; add new article

(defn create-success! [{:keys [article]}]
  (reset! submitting-state false)
  (reset! current-article-state article))

(defn create-error! [{{:keys [errors]} :response}]
  (reset! submitting-state false)
  (reset! error-state errors))

(defn create-article! [article]
  (reset! submitting-state true)
  (POST (str api-uri "/articles")
        {:params {:article article}
         :handler create-success!
         :error-handler create-error!
         :headers (get-auth-header)
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})}))
