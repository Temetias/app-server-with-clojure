(ns app_server.core
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [routes GET]]
            [compojure.route :as route]
            [watch.man :refer [watch!]]
            [clojure.java.io :as io]
            [app_server.render :as render]))

;; constants

(def port 8080)
(def apps-folder "C:/Users/teemu/clojure-server/apps/")

;; apps

(defn is-dir? [path]
  (.isDirectory (io/file path)))

(defn sequable-apps []
  (seq (.list (io/file apps-folder))))

(defn read-apps []
  (filter (fn [path] (is-dir? (str apps-folder path))) (sequable-apps)))

;; routing

(defn app-to-route-mapper [apps]
  (fn [app]
    (GET (str "/" app) [:as req]
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    (render/base-page (slurp (str apps-folder app "/index.html")) apps)})))

(defn index-route [apps]
  (GET "/" [:as req]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body     (render/base-page (slurp (str render/page-template-folder "/index.html")) apps)}))

(defn router [apps]
  (apply routes (conj
    (map (app-to-route-mapper apps) apps)
    (index-route apps)
    (route/resources "/"))))

;; server

(defn resetable-server []
  (println (str "Starting server on port " port))
  (let [server-killer (atom (run-server (router (read-apps)) {:port port}))]
    (fn [apps]
      (println "Resetting server...")
      (@server-killer :timeout 0)
      (reset! server-killer (run-server (router apps) {:port port})))))

(defn -main [& args]
  (let [reset-server (resetable-server)]
    (watch! apps-folder
      (fn [_]
        (reset-server (read-apps))))))
