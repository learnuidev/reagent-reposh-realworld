(ns app.comments
  (:require [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [app.api :refer [api-uri get-auth-header]]
            [ajax.core :refer [GET POST PUT DELETE json-request-format json-response-format]]))

(defonce comments-state (r/atom nil))
(defonce loading-state (r/atom false))
(defonce submitting-state (r/atom false))
(defonce error-state (r/atom nil))

(comment
   @error-state)

(defn add-error! [{{:keys [errors]} :response}]
 (reset! submitting-state false)
 (reset! error-state errors))

(defn add-success! [{:keys [comment]}]
  (reset! comments-state (conj @comments-state comment))
  (reset! submitting-state false)
  (reset! error-state nil))

(defn add-comment! [slug comment]
  (reset! submitting-state true)
  (POST (str api-uri "/articles/" slug "/comments")
        {:params {:comment comment}
         :handler add-success!
         :error-handler add-error!
         :headers (get-auth-header)
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})}))


(defn browse-success! [{:keys [comments]}]
   (reset! loading-state false)
   (reset! comments-state comments))

(comment
  @comments-state)

(defn browse-error! [{{:keys [errors]} :response}]
  (js/console.log "ERROR" errors)
  (reset! loading-state false))

(defn browse-comments [slug]
  (reset! loading-state true)
  (GET (str api-uri "/articles/" slug "/comments")
       {:handler browse-success!
        :error-handler browse-error!
        :headers (get-auth-header)
        :format (json-request-format)
        :response-format (json-response-format {:keywords? true})}))

(comment
  (browse-comments "Learning-Clojure-374"))


(defn delete-success! [id]
  (let [filtered-comments (filter #(not= (:id %) id) @comments-state)]
    (reset! comments-state filtered-comments)))


(defn delete-error! [error]
  (println error))

(defn delete-comment! [slug id]
  (DELETE (str api-uri "/articles/" slug "/comments/" id)
    {:handler #(delete-success! id)
     :error-handler delete-error!
     :headers (get-auth-header)
     :response-format (json-response-format {:keywords? true})}))
