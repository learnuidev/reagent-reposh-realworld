(ns app.pages.article-details
  (:require [app.articles :refer [current-article-state loading-state delete-article!]]
            [reitit.frontend.easy :as rfe]))

(defn article-actions [{{:keys [author slug] :as article} :article
                        can-modify? :can-modify?}]
  (when can-modify?
    [:span
     [:a.btn.btn-outline-secondary.btn-sm
      {:href (rfe/href :routes/edit-article {:slug slug})}
      [:i.ion-edit]
      "Edit Article"]
     [:button.btn.btn-outline-danger.btn-sm
      {:on-click #(delete-article! slug)}
      [:i.ion-trash-a]
      "Delete Article"]]))

(defn article-meta [{{:keys [author] :as article} :article
                     can-modify? :can-modify?}]
  [:div.article-meta
   [:a {:href (rfe/href :routes/profile {:username (:username author)})}
    [:img {:src (:image author) :alt (:username author)}]
    [:div.info
     [:a.author {:href (rfe/href :routes/profile {:username (:username author)})}
      (:username author)]
     [:span.date (.toDateString (js/Date. (:createdAt article)))]]
    [article-actions {:article article
                      :can-modify? can-modify?}]]])

(defn comments-container [])
(defn article-details-page []
  (if @loading-state
    [:div "loading article"]
    (if (seq @current-article-state)
      [:div.article-page
       [:div.banner>div.container
        [:h1 (:title @current-article-state)]
        [article-meta {:article @current-article-state
                       :can-modify? true}]]
       [:div.container.page
        [:div.row.article-content
         [:div.col-xs-12
          [:p (:body @current-article-state)]
          [:ul.tag-list
           (for [tag (:tagList @current-article-state)]
             ^{:key tag} [:li.tag-default.tag-pill.tag-outline tag])]]]
        [:hr]
        [:div.article-actions]
        [:div.row
         [comments-container]]]]
      [:div "Article Not found"])))
