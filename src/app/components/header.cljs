(ns app.components.header
  (:require [reitit.frontend.easy :as rfe]
            [goog.string :as gstring]))

(defn unauth-header []
  [:ul.nav.navbar-nav.pull-xs-right
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/home)} "Home"]]
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/login)} "Login"]]
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/register)} "Sign Up"]]])

(defn auth-header [{:keys [username image]}]
  [:ul.nav.navbar-nav.pull-xs-right
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/home)} "Home"]]
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/new-article)}
     [:i.ion-compose]
     (gstring/unescapeEntities "&nbsp;") "New Article"]]
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/settings)}
     [:i.ion-gear-a]
     (gstring/unescapeEntities "&nbsp;") "Settings"]]
   [:li.nav-item
    [:a.nav-link {:href (rfe/href :routes/profile {:username username})}
     [:img.user-pic {:src image}]
     username]]])

(defn header [auth-user]
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand {:href (rfe/href :routes/home)} "conduit"]
   (if auth-user
     [auth-header auth-user]
     [unauth-header])])
