(ns duct-play.component.arango-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [duct-play.component.arango-component :as arango-component]))

(defn db-cleanup-fixture [f]
  (let [arango (component/start (arango-component/arango-component "db" 8529))]
    (println ";; deleting database")
    (try
      (arango-component/delete-database arango "hello")
      (catch Exception e (println (str ";;" (.getMessage e)))))
    (f)
    (component/stop arango)))

(use-fixtures :each db-cleanup-fixture)

(deftest test-can-create-db
  (let [arango (component/start (arango-component/arango-component "db" 8529))]
      (is (arango-component/create-database arango "hello"))))

(deftest test-can-delete-db
  (let [arango (component/start (arango-component/arango-component "db" 8529))]
    (is (arango-component/create-database arango "hello"))
    (is (arango-component/delete-database arango "hello"))))
