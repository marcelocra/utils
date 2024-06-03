(ns dev.marcelocra.core
  (:require
   ["node:fs" :as fs]
   ["node:path" :as path]
   ["shelljs$default" :as shell]))

(set! *warn-on-infer* true)

(fs/readdirSync
 (path/resolve (-> (shell/pwd) (.toString))
               "templates"))

(defn something []
  (fs/readdirSync "."))

#js {:something something}