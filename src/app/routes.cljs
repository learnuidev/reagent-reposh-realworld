(ns app.routes
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.frontend.controllers :as rfc]
            [reitit.coercion.spec :as rss]
            ;; state
            [app.auth :as auth]
            ;; pages
            [app.pages.home :refer [home-page]]
            [app.pages.login :refer [login-page]]
            [app.pages.register :refer [register-page]]
            [app.pages.settings :refer [settings-page]]))

(defonce routes-state (r/atom nil))

(def routes
  [["/"         {:name :routes/home
                 :view #'home-page
                 :controllers [{:start #(js/console.log "enter - home page")
                                :stop #(js/console.log  "exit - home page")}]}]
   ["/login"    {:name :routes/login
                 :view #'login-page
                 :controllers [{:start #(js/console.log "enter - login page")
                                :stop #(js/console.log  "exit - login page")}]}]
   ["/register" {:name :routes/register
                 :view #'register-page
                 :controllers [{:start #(js/console.log "enter - register page")
                                :stop (fn []
                                        (js/console.log "leave - register page")
                                        (if (seq @auth/error-state)
                                          (js/console.log "Gets invoked again")
                                          (reset! auth/error-state nil)))}]}]
   ["/settings" {:name :settings
                 :view #'settings-page}]])

(comment "takes route name and generates the route path, nil if not found"
         (rfe/href ::login))

(defn router-start! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion
                             :controllers [{:start #(println "Root controller start")
                                            :stop #(println "Root controller stop")}]}})
   (fn [new-match] (swap! routes-state (fn [old-match]
                                         (if new-match
                                           (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
    ;; set to false to enable HistoryAPI
   {:use-fragment false}))
