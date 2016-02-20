(ns duct-play.endpoint.example.view
  (:use [hiccup.core]))

(defn render [content]
  (html
    [:div {:class "test"} content]))
