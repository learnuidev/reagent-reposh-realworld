(ns app.pages.profile
  (:require [app.profile :refer [profile-state]]))

(defn profile-page []
  (when @profile-state
    [:div.profile-page
     [:div.user-info>div.container
      [:div.row>div.col-xs-12.col-md-10.offset-md-1
       "Profile page: " (:username @profile-state)]]
     [:div.container>div.row>div.col-xs-12.col-md-10.offset-md-1
      [:div.articles-toggle "Article Toggle Comp"]
      [:div "Article List Comp"]]]))
