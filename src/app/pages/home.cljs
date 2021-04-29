(ns app.pages.home
  (:require
   [app.components.main-view :refer [main-view]]
   [app.components.banner :refer [banner]]))

(defn home-page []
  [:div.home-page
   [banner "conduit" "auth-user-token"]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3
     [:div.sidebar
      [:p "Popular Tags"]]]]])
