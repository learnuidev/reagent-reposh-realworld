(ns app.layout
  (:require
   [app.routes :refer [routes-state]]
   [app.auth :refer [auth-state]]
   ;; components
   [app.components.header :refer [header]]))

(comment
  @routes-state)
(defn app []
  [:div
   [header @auth-state]
   (let [current-view (-> @routes-state :data :view)]
     [current-view @routes-state])])
