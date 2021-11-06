(ns app.profile
  (:require [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [app.api :refer [api-uri]]
            [clojure.string :as s]
            [ajax.core :refer [GET POST PUT json-request-format json-response-format]]))

(defonce profile-state (r/atom nil))
(defonce error-state (r/atom nil))

(defn fetch-success! [{:keys [profile]}]
  (reset! profile-state profile))
(defn fetch-error! [error]
  (reset! error-state error))

(defn fetch! [username]
  (GET (str api-uri "/profiles/" username)
       {:handler fetch-success!
        :response-format (json-response-format {:keywords? true})
        :error-handler fetch-error!}))

;; testing
(comment
  @profile-state
  (fetch! "learnuidev2021"))
