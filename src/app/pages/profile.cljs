(ns app.pages.profile
  (:require [app.profile :refer [profile-state follow! unfollow!]]
            [app.auth :refer [auth-state]]
            [goog.string :as gstring]
            [reitit.frontend.easy :as rfe]))

(defn edit-profile-settings [user?]
  (when user?
    [:a.btn.btn-sm.btn-outline-secondary.action-btn
     {:href (rfe/href :routes/settings)}
     [:i.ion-gear-a]
     (gstring/unescapeEntities "&nbsp;")
     "Edit Profile Settings"]))

(defn toggle-follow [{:keys [following username]}]
  (if following
    (unfollow! username)
    (follow! username)))

(defn follow-user-button [user? user]
  (when-not user?
    [:button {:class ["btn btn-sm action-btn" (if (:following user) "btn-secondary" "btn-outline-secondary")]
              :on-click #(toggle-follow user)}
     [:i.ion-plus-round]
     (gstring/unescapeEntities "&nbsp;")
     (if (:following user) "Unfollow" "Follow")]))

(defn profile-page []
  (let [user?   (= (:username @auth-state)
                   (:username @profile-state))]
    (when @profile-state
      [:div.profile-page
       [:div.user-info>div.container
        [:div.row>div.col-xs-12.col-md-10.offset-md-1
         [:img.user-img {:src (:image @profile-state)}]
         [:h4 (:username @profile-state)]
         [:p (:bio @profile-state)]
         [edit-profile-settings user?]
         [follow-user-button user? @profile-state]]]
       [:div.container>div.row>div.col-xs-12.col-md-10.offset-md-1
        [:div.articles-toggle
         [:ul.nav.nav-pills.outline-active
          [:li.nav-item
           [:a.nav-link.active
            "My Articles"]]
          [:li.nav-item
           [:a.nav-link
            "Favourited Articles"]]]]
        [:div "Article List Comp"]]])))
