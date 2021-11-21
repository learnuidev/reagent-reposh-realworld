(ns app.pages.article-details
  (:require [app.articles :refer [current-article-state loading-state delete-article!]]
            [app.auth :as auth]
            [reagent.core :as r]
            [app.comments :as comments]
            [app.components.list-errors :refer [list-errors]]
            [goog.string :as gstring]
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


(comment
   @comments/comments-state)
(defn comment-item [{slug :slug
                     auth-user :auth-user
                     {:keys [author body createdAt id]} :comment}]
  (let [author? (= (:username auth-user) (:username author))
        href (rfe/href :routes/profile {:username (:username author)})]
    [:div.card
     [:div.card-block
      [:p.card-text body]]
     [:div.card-footer
      [:a.comment-author {:href href}
       [:img.comment-author-img {:src (:image author)
                                 :alt (:username author)}]]
      (gstring/unescapeEntities "&nbsp;")
      [:a.comment-author {:href href}
       (:username author)]
      [:span.date-posted
       (.toDateString (new js/Date createdAt))]
      (when author?
        [:span.mod-options
          [:i.ion-trash-a {:on-click #(comments/delete-comment! slug id)}]])]]))

(defn comments-list [{:keys [comments slug auth-user loading?]}]
  (if loading?
    [:div "Loading comments..."]
    [:div
     (for [comment comments]
       ^{:key (:id comment)}
       [comment-item {:comment comment
                      :auth-user auth-user
                      :slug slug}])]))

(defn comment-input [{:keys [slug auth-user]}]
  (let [initial-state {:body ""}
        state (r/atom initial-state)
        reset-comment-input #(reset! state initial-state)]
    (fn []
      [:form.card.comment-form {:on-submit (fn [event]
                                             (.preventDefault event)
                                             (comments/add-comment! slug @state)
                                             (reset-comment-input))}
       [:div.card-block
        [:textarea.form-control
         {:placeholder "Write a comment"
          :rows 3
          :value (:body @state)
          :on-change #(swap! state assoc :body (.. % -target -value))}]]
       [:div.card-footer
        [:img.comment-author-img
         {:src (:image auth-user)
          :alt (:username auth-user)}]
        [:button.btn.btn-sm.btn-primary
         {:type :submit
          :disabled @comments/submitting-state}
         "Post comment"]]])))

(defn comments-container [{:keys [auth-user slug]}]
  (if auth-user
    [:div.col-xs-12.col-md-8.offset-md-2
     [list-errors @comments/error-state]
     [comment-input {:slug slug
                     :auth-user auth-user}]
     [comments-list {:slug slug
                     :auth-user auth-user
                     :comments @comments/comments-state
                     :loading? @comments/loading-state}]]
    [:div.col-xs-12.col-md-8.offset-md-2
     [:p
      [:a {:href (rfe/href :routes/login)} "Login"]
      " or "
      [:a {:href (rfe/href :routes/register)} "Sign Up"]
      (gstring/unescapeEntities "&nbsp;") "to add comments to this article"]
     [comments-list {:slug slug
                     :auth-user auth-user
                     :comments @comments/comments-state
                     :loading? @comments/loading-state}]]))
; const canModify = this.props.currentUser &&
;   this.props.currentUser.username === this.props.article.author.username;


(defn article-details-page []
  (let [can-modify? (= (:username @auth/auth-state)
                       (-> @current-article-state :author :username))]
    (if @loading-state
      [:div "loading article"]
      (if (seq @current-article-state)
        [:div.article-page
         [:div.banner>div.container
          [:h1 (:title @current-article-state)]
          [article-meta {:article @current-article-state
                         :can-modify? can-modify?}]]
         [:div.container.page
          [:div.row.article-content
           [:div.col-xs-12
            [:p (:body @current-article-state)]
            [:ul.tag-list
             (for [tag (:tagList @current-article-state)]
               ^{:key tag} [:li.tag-default.tag-pill.tag-outline tag])]]]
          [:hr]
          [:div.article-actions
           [article-meta {:article @current-article-state
                          :can-modify? can-modify?}]]
          [:div.row
           [comments-container {:auth-user @auth/auth-state
                                :slug (:slug @current-article-state)}]]]]
        [:div "Article Not found"]))))
