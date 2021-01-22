(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]))

(defn app []
  ;; vector -> data
  [:h1 "Hello, World!"])

(defn ^:dev/after-load render
  "Render the toplevel component for this app.
   Dev Mode - runs every time the required dependencies changes"
  []
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic. Runs only once"
  []
  (render))

(js/console.log "Conduit")
