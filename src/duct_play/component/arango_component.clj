(ns duct-play.component.arango-component
  (:require [com.stuartsierra.component :as component])
  (:import (com.arangodb ArangoConfigure ArangoDriver ArangoHost)))

(defn- configuration-from [host port]
  (let [config (ArangoConfigure.)]
    (.setArangoHost config (ArangoHost. host port))
    (.init config)
    config))

(defn- connect-to-database [host port]
  (let [driver (ArangoDriver. (configuration-from host port))]
    driver))

(defprotocol DatabaseActions
  (create-database [arango database-name])
  (delete-database [arango database-name]))

(defrecord ArangoComponent [host port]
  component/Lifecycle
  (start [component]
    (if-not (:driver component)
      (do
        (println ";; Starting database")
        (let [driver (connect-to-database host port)]
          (assoc component :driver driver)))
      component))

  (stop [component]
    (dissoc component :driver))

  DatabaseActions
  (create-database [component database-name]
    (.createDatabase (:driver component) database-name nil))

  (delete-database [component database-name]
    (.deleteDatabase (:driver component) database-name)))

(defn arango-component [host port]
  (map->ArangoComponent {:host host, :port port}))


