(ns hygd.init
  (:require hygd.server
            hygd.logging
            hygd.nrepl))

(def init-fns [#'hygd.logging/init
               #'hygd.nrepl/init
               #'hygd.server/init])

(defn pretty-now []
  (.toLocaleString (java.util.Date.)))

(defn init []
  (doseq [f init-fns]
    (println (pretty-now) f)
    (f)))

(defn -main []
  (init)
  (println (pretty-now) "done"))
