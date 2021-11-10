(ns app.components.main-view
  (:require
   [app.articles :refer [articles-state loading-state articles-feed articles-browse]]
   [app.components.articles :refer [articles]]
   [reagent.core :as r]))

(defonce tab-state (r/atom :all))

(defn handle-feed []
  (reset! tab-state :feed)
  (articles-feed))

(defn handle-all []
  (reset! tab-state :all)
  (articles-browse))

(defn feed-toggle []
  [:div.feed-toggle
   [:ul.nav.nav-pills.outline-active
    [:li.nav-item
     [:a {:class ["nav-link" (when (= @tab-state :feed) "active")]
          :on-click handle-feed}
      "Your Feed"]]
    [:li.nav-item
     [:a {:class ["nav-link" (when (= @tab-state :all) "active")]
          :on-click handle-all}
      "Global Feed"]]]])

(defn main-view []
  [:div.col-md-9
   [feed-toggle]
   [articles {:articles (:articles (deref articles-state))
              :loading? @loading-state}]])
