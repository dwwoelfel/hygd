(ns hygd.server
  (:require [clj-time.core :as time]
            [clj-time.coerce :refer (from-long)]
            [clj-time.format :as time-format]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.pprint]
            [clojure.string]
            [clojure.tools.logging :as log]
            [compojure.handler :refer (site)]
            [compojure.core :refer (POST GET defroutes)]
            [inflections.core :refer (pluralize)]
            [org.httpkit.server :refer (run-server)]
            [slingshot.slingshot :refer (try+)]
            [hiccup.page :refer (html5)]
            [hiccup-bridge.core :refer (html->hiccup)]
            [net.cgrand.enlive-html :as enlive]))

(defn enlive->str [enlive-content]
  (apply str (enlive/emit* enlive-content)))

(defn update-attrs [enlive-content blacklist]
  (enlive/at enlive-content
             [:*] (fn [node]
                    (if (seq (:attrs node))
                      (let [old-attrs (:attrs node)
                            new-attrs (if (:id old-attrs)
                                        (assoc old-attrs :class (clojure.string/replace (:id old-attrs) #"[_\d]+$" ""))
                                        old-attrs)
                            new-attrs (if (:d new-attrs)
                                        (update-in new-attrs [:d] clojure.string/replace #"\s+" " ")
                                        new-attrs)
                            new-attrs (apply dissoc new-attrs (conj blacklist :id))]
                        (assoc node :attrs new-attrs))
                      node))))

(defn remove-empty-g [enlive-content]
  (enlive/at enlive-content
             [:g] (fn [node]
                    (if (empty? (:attrs node))
                      (enlive/unwrap node)
                      node))))

(defn abcd [svg]
  (-> svg
      (java.io.StringReader.)
      (enlive/html-resource)
      (enlive/select [:svg])))

(defn svg->hiccup [svg blacklist]
  (-> svg
      (java.io.StringReader.)
      (enlive/html-resource)
      (enlive/select [:svg])
      (update-attrs blacklist)
      (remove-empty-g)
      (enlive->str)
      (html->hiccup)))

(defroutes all-routes
  (GET "/ping" [] (fn [req] {:status 200 :body "pong"}))
  (GET "/" [] (fn [req]
                {:status 200
                 :body (html5 {}
                              [:body [:form {:method "POST" :action "/"}
                                      [:div [:label {:for "svg"} "svg:"]]
                                      [:div [:textarea {:name "svg" :cols 100 :rows 20}]]
                                      [:br]
                                      [:div [:label {:for "blacklist"} "attributes blacklist (comma separated): "]]
                                      [:div [:input {:name "blacklist" :style "width: 1000px" :type "text" :value "fill,font-family,font-size,stroke,stroke-width,stroke-linecap,stroke-miterlimit,stroke-linejoin,display"}]]
                                      [:br]
                                      [:div [:input {:type "submit" :value "Convert"}]]]])}))
  (POST "/" []
        (fn [req]
          (let [svg (-> req :form-params (get "svg"))
                blacklist (-> req :form-params (get "blacklist") (clojure.string/split #",") (#(map (comp keyword clojure.string/trim) %)))]
            {:status 200
             :body (with-out-str
                     (binding [clojure.pprint/*print-right-margin* 100]
                       (doall (map clojure.pprint/pprint (svg->hiccup svg blacklist)))))}))))

(defn port []
  (cond (System/getenv "HTTP_PORT") (Integer/parseInt (System/getenv "HTTP_PORT"))
        :else 8090))

(defn start []
  (def server (run-server (site #'all-routes) {:port (port)})))

(defn stop []
  (server))

(defn restart []
  (stop)
  (start))

(defn init []
  (start))
