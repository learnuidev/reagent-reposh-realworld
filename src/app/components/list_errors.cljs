(ns app.components.list-errors
  (:require [clojure.string :as s]))

(defn list-errors [errors]
  (when (seq errors)
    [:ul.error-messages
     (for [[key value] errors]
       ^{:key key} [:li (str (s/capitalize (name key)) " - " (s/join ", " value))])]))
