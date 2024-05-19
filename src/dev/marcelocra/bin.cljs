(ns dev.marcelocra.bin
  (:require
   [dev.marcelocra.core :as lib]

   ["node:fs" :as fs]
   ["node:path" :as path]
   ["shelljs$default" :as sh]))

(def source-path (path/resolve "./src/dev/marcelocra"))
(def templates-folder (path/resolve source-path "templates"))
(def templates (fs/readdirSync (path/resolve templates-folder)))

(def args (-> js/process.argv
              (.slice 2)
              js->clj
              not-empty))

(defn main []
  (let [args-set (set args)]
    (println "These are the arguments passed to this script:" args-set)))

(main)