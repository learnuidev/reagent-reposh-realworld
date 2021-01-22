(ns app.core
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [spec-tools.data-spec :as ds]
            [ajax.core :refer [GET json-response-format]]))

;; aricles state
(defonce articles-state (r/atom nil))

;; URI
(defonce api-uri "https://conduit.productionready.io/api")

(defn handler [response]
  (reset! articles-state response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn articles-browse []
  (GET (str api-uri "/articles?limit=20")
       {:handler handler
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (articles-browse)
  (first (:articles (deref articles-state))))

(defn article-preview [{:keys [title description favoritesCount author createdAt tagList]}]
  [:div.article-preview
   [:div.article-meta
    [:a
     [:img {:src (:image author)}]]
    [:div.info
     [:a.author (:username author)]
     [:span.date (.toDateString (new js/Date createdAt))]]
    [:div.pull-xs-right
     [:button.btn.btn-sm.btn-outline-primary
      [:i.ion-heart favoritesCount]]]]
   [:a.preview-link
    [:h1 title]
    [:p description]
    [:span "Read more..."]
    [:ul.tag-list
     (for [tag tagList]
       ^{:key tag} [:li.tag-default.tag-pill.tag-outline tag])]]])

(defn articles [items]
  (if-not (seq items)
    [:div.article-preview "Loading..."]
    (if (= 0 (count items))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} items]
         ^{:key slug} [article-preview article])])))

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand {:href (rfe/href ::home)} "conduit"]
   [:ul.nav.navbar-nav.pull-xs-right
    [:li.nav-item
     [:a.nav-link {:href (rfe/href ::home)} "Home"]]
    [:li.nav-item
     [:a.nav-link {:href (rfe/href ::login)} "Login"]]]])

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
   [articles (:articles (deref articles-state))]])

;; 4. Routes ====


;; Step 4.1 - Define Pages - DONE
(defn home-page []
  [:div.home-page
   [banner "conduit" "auth-user-token"]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3
     [:div.sidebar
      [:p "Popular Tags"]]]]])

(defn auth-signin [event]
  (.preventDefault event)
  (js/console.log "LOGIN"))

(defn login-page []
  [:div.auth-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Sign In"]
    [:p.text-xs-center [:a "Need an account?"]]
    [:form {:on-submit auth-signin}
     [:fieldset
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :email :placeholder "john@gmail.com"}]]
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :password :placeholder "Password"}]]
      [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign In"]]]]])

;; Step 4.2 - Install reitit - DONE
;; Step 4.3 - require necessary reitit dependencies - DONE
;; Step 4.4 - Define routes
(def routes
  [["/"      {:name ::home
              :view #'home-page}]
   ["/login" {:name ::login
              :view #'login-page}]])

;; Step 4.5 - Define routing state
(defonce routes-state (r/atom nil))

(comment "takes route name and generates the route path, nil if not found"
         (rfe/href ::login))

;; Step 4.6 - write the router-start! function that starts the router
(defn router-start! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [matched-route] (reset! routes-state matched-route))
    ;; set to false to enable HistoryAPI
   {:use-fragment false}))

(comment)

(defn app []
  [:div
   [header]
   (let [current-view (-> @routes-state :data :view)]
     [current-view])])

(defn ^:dev/after-load render
  "Render the toplevel component for this app.
   Dev Mode - runs every time the required dependencies changes"
  []
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic. Runs only once"
  []
  (router-start!)
  (articles-browse)
  (render))
