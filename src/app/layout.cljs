(ns app.layout
  (:require
   ["react" :as react]
   [reagent.core :as r]
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
     [:> react/Suspense {:fallback (r/as-element [:div "Loading ..."])}
        [:> current-view @routes-state]])])
