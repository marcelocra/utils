(ns dev.marcelocra.core
  (:require
   ["node:fs" :as fs]))

(set! *warn-on-infer* true)

(defn something []
  (fs/readdirSync "."))

#js {:something something}