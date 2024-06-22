(ns dev.marcelocra.lib)

(defn play []
  (println "this is a core file"))

(def exports #js {:play play})