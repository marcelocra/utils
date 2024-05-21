(ns core
  (:require
   [reagent.core :as r]
   ["react-dom/client" :refer [createRoot]]))

(defonce state (r/atom {:count 0}))
(defonce root (createRoot (js/document.getElementById "root")))

(defn app []
  [:div {:class "bg-slate-200 rounded-lg p-3"}
   [:div {:key "h1"}
    [:h1 "hello world"]]
   [:div
    [:div "your count is: " (do (println "hey") (:count @state))]]
   [:button.btn.btn-primary.uppercase {:on-click #(swap! state update :count inc)} "inc"]])

(comment
  (swap! state assoc :count 10)

  ;; rcf
  )

(defn ^:dev/after-load load-app []
  (.setAttribute
   (.-body js/document)
   "class"
   "container min-w-[700] min-h-screen mx-auto")
  (.render root (r/as-element [app])))

(defn main []
  (load-app))
