(ns app.components.header
  (:require [reitit.frontend.easy :as rfe]))

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand {:href (rfe/href :routes/home)} "conduit"]
   [:ul.nav.navbar-nav.pull-xs-right
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :routes/home)} "Home"]]
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :routes/login)} "Login"]]
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :routes/register)} "Sign Up"]]]])
