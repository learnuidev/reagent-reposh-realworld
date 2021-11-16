(ns app.components.articles
  (:require [reitit.frontend.easy :as rfe]))

(defn article-preview [{:keys [slug title description favoritesCount author createdAt tagList]}]
  [:div.article-preview
   [:div.article-meta
    [:a {:href (rfe/href :routes/profile {:username (:username author)})}
     [:img {:src (:image author)}]]
    [:div.info
     [:a.author {:href (rfe/href :routes/profile {:username (:username author)})}
      (:username author)]
     [:span.date (.toDateString (new js/Date createdAt))]]
    [:div.pull-xs-right
     [:button.btn.btn-sm.btn-outline-primary
      [:i.ion-heart favoritesCount]]]]
   [:a.preview-link {:href (rfe/href :routes/article {:slug slug})}
    [:h1 title]
    [:p description]
    [:span "Read more..."]
    [:ul.tag-list
     (for [tag tagList]
       ^{:key tag} [:li.tag-default.tag-pill.tag-outline tag])]]])

(defn articles [{:keys [articles loading?]}]
  (if loading?
    [:div.article-preview "Loading..."]
    (if (= 0 (count articles))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} articles]
         ^{:key slug} [article-preview article])])))
