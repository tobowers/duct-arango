(ns duct-play.system
  (:require [com.stuartsierra.component :as component]
            [duct.component.endpoint :refer [endpoint-component]]
            [duct.component.handler :refer [handler-component]]
            [duct.middleware.not-found :refer [wrap-not-found]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [duct-play.endpoint.example.route :refer [example-endpoint]]
            [duct-play.endpoint.base :refer [base-endpoint]]))

(def base-config
  {:app {:middleware [[wrap-not-found :not-found]
                      [wrap-defaults :defaults]]
         :not-found  "Resource Not Found"
         :defaults   (meta-merge api-defaults {})}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app  (handler-component (:app config))
         :http (jetty-server (:http config))
         :example (endpoint-component example-endpoint)
         :base (endpoint-component base-endpoint))
        (component/system-using
         {:http [:app]
          :app  [:example :base]
          :example []}))))
