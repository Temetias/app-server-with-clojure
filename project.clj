(defproject app_server "0.0.1"
  :author "Teemu Karppinen"
  :description "A hot reloading http server to serve static apps with."
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.0"]
                 [spootnik/watchman "0.3.7"]]
  :main app_server.core)