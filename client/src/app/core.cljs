(ns app.core
  (:require [rum.core :as rum]
            [cljsjs.moment :as moment]))

(enable-console-print!)

(rum/defc git-event [{:keys [id type repo created_at] :as event}]
  [:tr
   [:td type]
   [:td [:a {:href (-> repo :url)} (-> repo :name)]]
   [:td (.fromNow (js/moment created_at))]])

(rum/defc git-events [events]
  [:table
   [:thead
    [:tr
     [:th "Event Type"]
     [:th "Repo Name"]
     [:th "Timestamp"]]]
   [:tbody
    (map (fn [event] (rum/with-key (git-event event) (:id event))) events)]])

(defn mount [component element]
  (rum/mount component (.querySelector js/document element)))

(defn main []
  (do
    (-> (js/fetch "https://api.github.com/users/yeehaa123/events")
        (.then #(.json %1))
        (.then #(js->clj %1 :keywordize-keys true))
        (.then #(mount (git-events %1) "#git-container1")))))
