(ns duct-play.endpoint.example.route
  (:require [compojure.core :refer :all]
            [duct-play.endpoint.example.view :as view]))

(defn example-endpoint [config]
  (context "/example" []
    (GET "/" []
      (view/render "Example"))))
