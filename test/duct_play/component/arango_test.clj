(ns duct-play.component.arango-test
  (:require [com.stuartsierra.component :as component]
            [clojure.test :refer :all]
            [duct-play.component.arango :as arango]))

(defn db-cleanup-fixture [f]
  (let [driver (component/start (arango/arango "db" 8529))]
    (println ";; deleting database")
    (try
      (arango/delete-database driver "hello")
      (catch Exception e (println (str ";;" (.getMessage e))))))
  (f))

(use-fixtures :each db-cleanup-fixture)

(deftest test-can-create-db
  (let [driver (component/start (arango/arango "db" 8529))]
      (is (arango/create-database driver "hello"))))

(deftest test-can-delete-db
  (let [driver (component/start (arango/arango "db" 8529))]
    (is (arango/create-database driver "hello"))
    (is (arango/delete-database driver "hello"))))
