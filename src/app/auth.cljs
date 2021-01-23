(ns app.auth
  (:require [reagent.core :as r]))

(defonce auth-state (r/atom nil))

(defn register! [event]
  (.preventDefault event)
  (js/console.log "AUTH REGISTER"))

(defn login! [event]
  (.preventDefault event)
  (js/console.log "AUTH LOGIN"))
