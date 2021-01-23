(ns app.components.header
  (:require [reitit.frontend.easy :as rfe]))

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand {:href (rfe/href :home)} "conduit"]
   [:ul.nav.navbar-nav.pull-xs-right
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :home)} "Home"]]
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :login)} "Login"]]
    [:li.nav-item
     [:a.nav-link {:href (rfe/href :register)} "Sign Up"]]]])
