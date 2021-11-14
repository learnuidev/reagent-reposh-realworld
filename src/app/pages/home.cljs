(ns app.pages.home
  (:require
   [app.tags :refer [tags-state loading-state]]
   [app.articles :as articles :refer [tag-state tab-state]]
   [app.components.main-view :refer [main-view]]
   [app.components.banner :refer [banner]]))

(defn handle-tag [tag]
  (reset! tag-state tag)
  (reset! tab-state nil)
  (articles/articles-by-tag tag))

(comment
  @tag-state)

(defn tags-list [{:keys [tags loading? handle-tag]}]
  (if loading?
    [:div "Loading tags..."]
    (when (seq tags)
      [:div.tags-list
       (for [tag tags]
         ^{:key tag}
         [:button.tag-default.tag-pill
          {:on-click #(handle-tag tag)}
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
                  :handle-tag handle-tag
                  :loading? @loading-state}]]]]])
