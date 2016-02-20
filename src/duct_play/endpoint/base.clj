(ns duct-play.endpoint.base
  (:use ring.util.response)
  (:require [compojure.core :refer :all]))

(defn base-endpoint [config]
  (context "/" []
    (GET "/" []
    (redirect "/example"))))
