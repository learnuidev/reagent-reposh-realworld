(ns app.pages.new-article
  (:require [reagent.core :as r]
            [app.components.list-errors :refer [list-errors]]
            [app.articles :refer [create-article! submitting-state error-state]]))

; (defonce state (r/atom nil))

(comment
  @state)

(defn update-tags [state event]
  (-> state
      (update :tagList conj (.. event -target -value))
      (assoc :tag-input "")))

(defn add-new-article [article]
  (create-article! article))

(defn editor [{:keys [initial-state]}]
  (let [state (r/atom initial-state)
        watch-for-enter (fn [event]
                          (when (= 13 (.-keyCode event))
                            (swap! state #(update-tags % event))))
        remove-tag (fn [tag]
                     (swap! state assoc :tagList (disj (:tagList @state) tag)))]
    (fn []
      [:div.editor-page>div.container.page>div.row
       [:div.col-md-10.offset-md-1.col-xs-12
        [list-errors @error-state]
        [:form
         [:fieldset
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:placeholder "Article Title"
             :type :text
             :value (:title @state)
             :on-change #(swap! state assoc :title (.. % -target -value))}]]
          [:fieldset.form-group
           [:input.form-control
            {:placeholder "What's this article about?"
             :type :text
             :value (:description @state)
             :on-change #(swap! state assoc :description (.. % -target -value))}]]
          [:fieldset.form-group
           [:textarea.form-control
            {:placeholder "Write your article (in markdown)"
             :rows 8
             :value (:body @state)
             :on-change #(swap! state assoc :body (.. % -target -value))}]]
          [:fieldset.form-group
           [:input.form-control
            {:placeholder "Enter tags"
             :type :text
             :value (:tag-input @state)
             :on-change #(swap! state assoc :tag-input (.. % -target -value))
             :on-key-up watch-for-enter}]
           [:div.tag-list
            (when (seq (:tagList @state))
              (for [tag  (:tagList @state)]
                ^{:key tag} [:span.tag-default.tag-pill
                             [:i.ion-close-round {:on-click #(remove-tag tag)}]
                             tag]))]]
          [:button.btn.btn-lg.pull-xs-right.btn-primary
           {:disabled @submitting-state
            :type :button
            :on-click (fn [evt]
                        (.preventDefault evt)
                        (add-new-article @state))}
           "Publish Article"]]]]])))

(defn new-article-page []
  (let [initial-state {:title ""
                       :body ""
                       :description ""
                       :tag-input ""
                       :tagList #{}}]
    [editor {:initial-state initial-state}]))
