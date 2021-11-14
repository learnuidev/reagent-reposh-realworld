(ns app.components.main-view
  (:require
   [app.articles :refer [articles-state tab-state tag-state loading-state articles-feed articles-browse]]
   [app.components.articles :refer [articles]]
   [reagent.core :as r]))

(defn handle-feed []
  (reset! tab-state :feed)
  (reset! tag-state nil)
  (articles-feed))

(defn handle-all []
  (reset! tab-state :all)
  (reset! tag-state nil)
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
      "Global Feed"]]
    (when @tag-state
      [:li.nav-item
       [:a.nav-link.active
        [:i.ion-pound @tag-state]]])]])

(defn main-view []
  [:div.col-md-9
   [feed-toggle]
   [articles {:articles (:articles (deref articles-state))
              :loading? @loading-state}]])
