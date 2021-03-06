(ns app.components.articles
  (:require [reitit.frontend.easy :as rfe]
            [app.articles :refer [favourite-article! unfavourite-article! submitting-state]]))

;;
; const FAVORITED_CLASS = 'btn btn-sm btn-primary';
; const NOT_FAVORITED_CLASS = 'btn btn-sm btn-outline-primary';

(defn toggle-favourite! [article]
  (if (:favorited article)
    (unfavourite-article! article)
    (favourite-article! article)))
(defn article-preview [{:keys [slug favorited title description favoritesCount author createdAt tagList]
                        :as article}]
  [:div.article-preview
   [:div.article-meta
    [:a {:href (rfe/href :routes/profile {:username (:username author)})}
     [:img {:src (:image author)}]]
    [:div.info
     [:a.author {:href (rfe/href :routes/profile {:username (:username author)})}
      (:username author)]
     [:span.date (.toDateString (new js/Date createdAt))]]
    [:div.pull-xs-right
     [:button
      {:on-click #(toggle-favourite! article)
       :disabled @submitting-state
       :class ["btn btn-sm" (if (:favorited article) "btn-primary" "btn-outline-primary")]}
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
