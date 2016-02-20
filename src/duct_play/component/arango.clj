(ns duct-play.component.arango
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

(defrecord Arango [host port driver user]
  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [component]
    (println ";; Starting database")
    ;; In the 'start' method, initialize this component
    ;; and start it running. For example, connect to a
    ;; database, create thread pools, or initialize shared
    ;; state.
    (let [driver (connect-to-database host port)]
      ;; Return an updated version of the component with
      ;; the run-time state assoc'd in.
      (assoc component :driver driver)))

  (stop [component]
    (println ";; Stopping database")
    ;; In the 'stop' method, shut down the running
    ;; component and release any external resources it has
    ;; acquired.
    (.close driver)
    ;; Return the component, optionally modified. Remember that if you
    ;; dissoc one of a record's base fields, you get a plain map.
    (assoc component :driver nil)))

(defn arango [host port]
  (map->Arango {:host host, :port port}))

(defn create-database [arango database-name]
  (let [driver (:driver arango)]
    (.createDatabase driver database-name nil )))

(defn delete-database [arango database-name]
  (let [driver (:driver arango)]
    (.deleteDatabase driver database-name)))