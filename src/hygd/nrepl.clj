(ns hygd.nrepl
  (:require [clojure.tools.nrepl.server :refer (start-server stop-server default-handler)]
            [cider.nrepl]))

(defn port []
  (when (System/getenv "NREPL_PORT") (Integer/parseInt (System/getenv "NREPL_PORT"))))

;; see cider.nrepl/cider-nrepl-handler
(def cider-middleware (map resolve cider.nrepl/cider-middleware))

(defn init []
  (when-let [port (port)]
    (println "Starting nrepl on port" port)
    (def server (start-server :port port :handler (apply default-handler cider-middleware)))))
