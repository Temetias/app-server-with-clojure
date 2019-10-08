(ns app_server.render)

(def page-template-folder "C:/Users/teemu/clojure-server/app-server/resources/page-template")

(defn html [content class tag]
  (str "<" tag " class='" class "'>" content "</" tag ">"))
  
(defn html-a [link name]
  (str "<a href='/" link "'>" name "</a>"))

(defn html-header [content]
  (html content "main-header" "header"))

(defn app-to-link [app]
  (html-a app app))

(defn app-links [apps]
  (html-header (apply str (conj (map app-to-link apps) (html-a "" "Home")))))

(defn base-page [page-content apps]
  (str
    (slurp (str page-template-folder "/start.html"))
    (app-links apps)
    page-content
    (slurp (str page-template-folder "/end.html"))))