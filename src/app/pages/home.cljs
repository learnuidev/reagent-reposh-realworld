(ns app.pages.home
  (:require
   [app.tags :refer [tags-state loading-state]]
   [app.components.main-view :refer [main-view]]
   [app.components.banner :refer [banner]]))

(defn tags-list [{:keys [tags loading?]}]
  (if loading?
    [:div "Loading tags..."]
    (when (seq tags)
      [:div.tags-list
       (for [tag tags]
         ^{:key tag}
         [:button.tag-default.tag-pill
          {:on-click #(js/console.log "TODO")}
          tag])])))

(defn home-page []
  [:div.home-page
   [banner "conduit" "auth-user-token"]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3
     [:div.sidebar
      [:p "Popular Tags"]
      [tags-list {:tags @tags-state
                  :loading? @loading-state}]]]]])
