(ns duct-play.component.arango-component
  (:require [com.stuartsierra.component :as component])
  (:import  (com.arangodb ArangoConfigure ArangoDriver ArangoHost))
  (:import  (com.arangodb.entity UserEntity)))

(defn- configuration-from [host port]
  (let [config (ArangoConfigure.)]
    (.setArangoHost config (ArangoHost. host port))
    (.init config)
    config))

(defn- connect-to-database [host port]
  (let [driver (ArangoDriver. (configuration-from host port))]
    driver))

(defrecord ArangoComponent [host port user]
  component/Lifecycle

  (start [component]
    (if-not (:driver component)
      (do
        (println ";; Starting database")
        (let [driver (connect-to-database host port)]
          (assoc component :driver driver)))
      component))

  (stop [component]
    (dissoc component :driver)))

(defn arango-component [host port]
  (map->ArangoComponent {:host host, :port port}))

(defn create-database [arango database-name]
  (let [driver (:driver arango)]
    (.createDatabase driver database-name nil)))

(defn delete-database [arango database-name]
  (let [driver (:driver arango)]
    (.deleteDatabase driver database-name)))