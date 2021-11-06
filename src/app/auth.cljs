(ns app.auth
  (:require [reagent.core :as r]
            [reitit.frontend.easy :as rfe]
            [app.api :refer [api-uri]]
            [clojure.string :as s]
            [ajax.core :refer [GET POST PUT json-request-format json-response-format]]))

;; Local Storage testing
(comment
  "Saving value into local storage"
  (.setItem js/localStorage
            "auth-user-token"
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTM4ODM2LCJ1c2VybmFtZSI6ImxlYXJudWlkZXYzQGZvby5jb20iLCJleHAiOjE2MTcxNjEwNzN9.O_cIsa66SQN26s3ap-F5oOQvp_zTpF4COH_HJDBK_cA"))
(comment
  "Getting auth value from local storage - will be used to make auth requests"
  (.getItem js/localStorage "auth-user-token"))
(comment
  "Removing value"
  (.removeItem js/localStorage "auth-user-token"))

;; ================= Auth State ===================
;; ================================================

(defonce auth-state (r/atom nil))
(defonce error-state (r/atom nil))
(comment
  @auth-state
  (s/join ", " ["error a", "error b"]))

(defn auth-success! [{{:keys [token] :as user} :user}]
  (.setItem js/localStorage "auth-user-token" token)
  (reset! auth-state user)
  (when (seq @error-state)
    (reset! error-state nil))
  (rfe/push-state :routes/home))

(defn auth-error! [{{:keys [errors]} :response}]
  (reset! error-state errors))

;; ================= Register ===================
;; ================================================
(defn register! [input]
  (POST (str api-uri "/users")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler auth-error!}))

(comment
  (register! {:username "learnuidev4@foo.com"
              :email "learnuidev4@foo.com"
              :password "learnuidev4@foo.com"}))

;; ================= Login ========================
;; ================================================
(defn login! [input] ;; {:email "" :password}
  (POST (str api-uri "/users/login")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler auth-error!}))
(comment
  (login! {:email "learnuidev4@foo.com"
           :password "learnuidev4@foo.com"}))

;; ================= Me ========================
(defn get-auth-header []
  (let [token (.getItem js/localStorage "auth-user-token")]
    [:Authorization (str "Token " token)]))

;; ================================================
;;
(defn get-me-success! [{user :user}]
  (reset! auth-state user))

(defn get-me-error! [error]
  (js/console.log "ERROR"))

(defn me []
  (GET (str api-uri "/user")
       {:handler get-me-success!
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler get-me-error!}))

;; save-user
(def example-user {:email ""
                   :password ""
                   :username ""
                   :image ""
                   :bio ""})

(defn save-user! [user] ;; {:email "" :password}
  (PUT (str api-uri "/user")
       {:params {:user user}
        :handler get-me-success!
        :error-handler get-me-error!
        :headers (get-auth-header)
        :format (json-request-format)
        :response-format (json-response-format {:keywords? true})}))

(comment
  (save-user! {:image "www.learnuidev123.com"
               :bio "I make videos on Clojure and ClojureScript"}))

(comment
  (me))
