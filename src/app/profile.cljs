(ns app.profile
  (:require [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [app.api :refer [api-uri get-auth-header]]
            [clojure.string :as s]
            [ajax.core :refer [GET POST PUT DELETE json-request-format json-response-format]]))

(defonce profile-state (r/atom nil))
(defonce tab-state (r/atom :author))
(defonce error-state (r/atom nil))

(defn fetch-success! [{:keys [profile]}]
  (reset! profile-state profile))
(defn fetch-error! [error]
  (reset! error-state error))

(defn fetch! [username]
  (GET (str api-uri "/profiles/" username)
       {:handler fetch-success!
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler fetch-error!}))

;; testing
(comment
  @profile-state
  (fetch! "learnuidev2021"))

;; follow user
(defn follow! [username]
  (POST (str api-uri "/profiles/" username "/follow")
        {:handler fetch-success!
         :headers (get-auth-header)
         :response-format (json-response-format {:keywords? true})
         :error-handler fetch-error!}))

;; follow user
(defn unfollow! [username]
  (DELETE (str api-uri "/profiles/" username "/follow")
          {:handler fetch-success!
           :headers (get-auth-header)
           :response-format (json-response-format {:keywords? true})
           :error-handler fetch-error!}))

(comment
  (follow! "Gerome")
  (unfollow! "Gerome"))
