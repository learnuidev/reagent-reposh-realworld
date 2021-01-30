(ns app.auth
  (:require [reagent.core :as r]
            [app.api :refer [api-uri error-handler]]
            [ajax.core :refer [GET POST json-request-format json-response-format]]))

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
(comment @auth-state)

(defn auth-success! [{{:keys [token] :as user} :user}]
  (.setItem js/localStorage "auth-user-token" token)
  (reset! auth-state user))

;; ================= Register ===================
;; ================================================
(defn register! [input]
  (POST (str api-uri "/users")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler error-handler}))

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
         :error-handler error-handler}))
(comment
  (login! {:email "learnuidev4@foo.com"
           :password "learnuidev4@foo.com"}))

;; ================= Me ========================
(defn get-auth-header []
  (let [token (.getItem js/localStorage "auth-user-token")]
    [:Authorization (str "Token " token)]))

;; ================================================
(defn me []
  (GET (str api-uri "/user")
       {:handler auth-success!
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))
(comment
  (me))
