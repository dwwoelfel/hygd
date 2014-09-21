(defproject hygd "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/pomegranate "0.2.0"]
                 [http-kit "2.1.14"]
                 [compojure "1.1.6"]
                 [cheshire "5.2.0"]
                 [clj-time "0.6.0"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [com.cemerick/url "0.1.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [slingshot "0.10.3"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [hiccup "1.0.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.16"]
                 [log4j/apache-log4j-extras "1.1"]
                 [org.slf4j/slf4j-api "1.6.2"]
                 [org.slf4j/slf4j-log4j12 "1.6.2"]
                 [inflections "0.8.2"]
                 [cider/cider-nrepl "0.7.0-SNAPSHOT"]
                 [clj-http "1.0.0"]
                 [hiccup-bridge "1.0.0-SNAPSHOT"]
                 [enlive "1.1.5"]]
  :min-lein-version "2.0.0"
  :main hygd.init)
