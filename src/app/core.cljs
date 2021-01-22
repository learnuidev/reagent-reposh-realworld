(ns app.core
  (:require [reagent.core :as r]))

;; Mock Data =====
(def mock-articles
  [{:title "Backpacking is fun"}
   {:title "Learn Docker"}
   {:title "Summer Plans 2021"}
   {:title "How to become a web developer"}
   {:title "How to Design Programs"}])

(defn articles [items]
  (if-not (seq items)
    [:div.article-preview "Loading..."]
    (if (= 0 (count items))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [article items]
         [:h2 (:title article)])])))

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand "conduit"]])

;; Home page =====
(defn banner [app-name token]
  (when token
    [:div.banner>div.container
     [:h1.logo-font app-name]
     [:p "A place to share your knowledge."]]))

;; Main View ==
(defn main-view []
  [:div.col-md-9
   [:div.feed-toggle
    [:ul.nav.nav-pills.outline-active
     [:li.nav-item
      [:a.nav-link.active {:href ""} "Global Feed"]]]]
   [articles]])

(defn home-page []
  [:div.home-page
   [banner "conduit" "auth-user-token"]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3
     [:div.sidebar
      [:p "Popular Tags"]]]]])

(defn app []
  [:div
   [header]
   [home-page]])

(defn ^:dev/after-load render
  "Render the toplevel component for this app.
   Dev Mode - runs every time the required dependencies changes"
  []
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic. Runs only once"
  []
  (render))
