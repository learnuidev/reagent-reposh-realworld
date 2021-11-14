(ns app.tags
  (:require [reagent.core :as r]
            [app.api :refer [api-uri get-auth-header]]
            [ajax.core :refer [GET json-response-format]]))

(defonce tags-state (r/atom nil))
(defonce loading-state (r/atom false))
(defonce error-state (r/atom nil))

(defn tags-browse-success [{:keys [tags]}]
  (reset! loading-state false)
  (reset! tags-state tags))

(defn tags-browse-error [error]
  (reset! loading-state false)
  (reset! error-state error))
(defn tags-browse []
  (reset! loading-state true)
  (GET (str api-uri "/tags")
       {:handler tags-browse-success
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler tags-browse-error}))

(defn reset-tags []
  (reset! tags-state nil))
(comment
  (tags-browse)
  @tags-state
  @loading-state)
