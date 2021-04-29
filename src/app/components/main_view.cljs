(ns app.components.main-view
  (:require
   [app.articles :refer [articles-state]]
   [app.components.articles :refer [articles]]))

(defn main-view []
  [:div.col-md-9
   [:div.feed-toggle
    [:ul.nav.nav-pills.outline-active
     [:li.nav-item
      [:a.nav-link.active {:href ""} "Global Feed"]]]]
   [articles (:articles (deref articles-state))]])
