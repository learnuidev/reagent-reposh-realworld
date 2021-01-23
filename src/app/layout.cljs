(ns app.layout
  (:require
   [app.routes :refer [routes-state]]
   ;; components
   [app.components.header :refer [header]]))

(defn app []
  [:div
   [header]
   (let [current-view (-> @routes-state :data :view)]
     [current-view])])
