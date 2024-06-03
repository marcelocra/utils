(ns dev.marcelocra.core
  (:require
   ["node:fs" :as fs]
   ["node:path" :as path]
   ["shelljs$default" :as shell]))

(set! *warn-on-infer* true)

(comment
  ;; works!
  (fs/readdirSync
   (path/resolve (-> (shell/pwd) (.toString))
                 "templates"))

  :rcf)

(defn something []
  (fs/readdirSync "."))

#js {:something something}