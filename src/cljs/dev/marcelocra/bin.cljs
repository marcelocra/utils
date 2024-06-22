(ns dev.marcelocra.bin
  (:require
  ;;  [dev.marcelocra.lib :as lib]
  ;;  ["fs" :as fs]
  ;;  ["shelljs$default" :as sh]
   ["path" :as path]))


(println "Hello from bin.cljs. Now it is:"
         (.toLocaleString (js/Date.) "pt-BR"))


(def source-path (path/resolve "./src/dev/marcelocra"))
(def templates-folder (path/resolve source-path "templates"))
;; (def templates (fs/readdirSync (path/resolve templates-folder)))

(def args (-> js/process.argv
              (.slice 2)
              js->clj
              not-empty))

(defn ^:export main []
  (let [args-set (set args)]
    (println "These are the arguments passed to this script:" args-set)))
