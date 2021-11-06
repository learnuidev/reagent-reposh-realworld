(ns app.pages.settings
  (:require [reitit.frontend.easy :as rfe]
            [app.auth :refer [auth-state save-user!]]
            [reagent.core :as r]))

(def initial-state {:email ""
                    :password ""
                    :username ""
                    :image ""
                    :bio ""})

(defn save-user [event user]
  (.preventDefault event)
  (save-user! user))

;; Next Step: Save users info
(comment
  @auth-state)
#_(def state (r/atom nil))

(comment
  @state)
(defn settings-form [user]
  #_(let [_ (reset! state user)])
  (let [state (r/atom user)]
    (fn []
      [:form {:on-submit #(save-user % @state)}
       [:fieldset
        [:fieldset.form-group
         [:input.form-control {:type :text
                               :placeholder "URL of profile picture"
                               :on-change #(swap! state assoc :image (.. % -target -value))
                               :value (:image @state)}]]
        [:fieldset.form-group
         [:input.form-control.form-control-lg
          {:type :text
           :placeholder "Username"
           :on-change #(swap! state assoc :username (.. % -target -value))
           :value (:username @state)}]]
        [:fieldset.form-group
         [:textarea.form-control.form-control-lg
          {:type :text
           :rows 8
           :placeholder "Short bio about you"
           :on-change #(swap! state assoc :bio (.. % -target -value))
           :value (:bio @state)}]]
        [:fieldset.form-group
         [:input.form-control.form-control-lg
          {:type :email
           :placeholder "Email"
           :on-change #(swap! state assoc :email (.. % -target -value))
           :value (:email @state)}]]
        [:fieldset.form-group
         [:input.form-control.form-control-lg
          {:type :password
           :placeholder "Password"
           :on-change #(swap! state assoc :password (.. % -target -value))
           :value (:password @state)}]]
        [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit}
         "Update Settings"]]])))

(defn logout []
  (.removeItem js/localStorage "auth-user-token")
  (rfe/push-state :routes/home)
  (reset! auth-state nil))

(defn settings-page []
  [:div.settings-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Your Settings"]
    (when @auth-state
      [settings-form @auth-state])
    [:hr]
    [:button.btn.btn-outline-danger
     {:on-click logout}
     "Or click here to logout."]]])
