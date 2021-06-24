(ns app.core
  (:require
   [reagent.core :as r]
   ;; Layout
   [app.layout :refer [app]]
   ;; Auth
   [app.auth :as auth]
   ;; router
   [app.routes :refer [router-start!]]
   ;; entity
   [app.articles :refer [articles-browse]]))

(defn ^:dev/after-load render
  "Render the toplevel component for this app.
   Dev Mode - runs every time the required dependencies changes"
  []
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic. Runs only once"
  []
  (router-start!)
  (articles-browse)
  (render))
