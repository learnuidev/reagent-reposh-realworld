(ns app.pages.register
  (:require
   [reitit.frontend.easy :as rfe]
   [clojure.string :as s]
   [reagent.core :as r]
   [app.auth :as auth :refer [error-state]]
   [app.components.list-errors :refer [list-errors]]))

(comment)

(defn register! [event registration-input]
  (.preventDefault event)
  (auth/register! registration-input))

(defn register-page []
  (let [initial-state {:email ""
                       :password ""
                       :username ""}
        state (r/atom initial-state)]
    (fn []
      [:div.auth-page>div.container.page>div.row
       [:div.col-md-6.offset-md-3.col-xs-12
        [:h1.text-xs-center "Sign Up"]
        [:p.text-xs-center [:a {:href (rfe/href :routes/login)} "Have an account?"]]
        [list-errors @error-state]
        [:form {:on-submit #(register! % @state)}
         [:fieldset
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :text
             :on-change #(swap! state assoc :username (.. % -target -value))
             :value (:username @state)
             :placeholder "Username"}]]]
         [:fieldset
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :email
             :value (:email @state)
             :on-change #(swap! state assoc :email (.. % -target -value))
             :placeholder "Email"}]]
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :password
             :on-change #(swap! state assoc :password (.. % -target -value))
             :value (:password @state)
             :placeholder "Password"}]]
          [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign Up"]]]]])))
