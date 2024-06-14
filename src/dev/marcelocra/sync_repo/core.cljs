#!/usr/bin/env nbb
(ns dev.marcelocra.sync-repo.core
  (:require
   ["path" :as path]
   ["fs" :as fs]))

(.-HOME js/process.env)

(def logfile-path (-> js/process.env
                      (.-HOME)
                      (path/resolve ".sync-repo.log.txt")))

(if (not (fs/existsSync logfile-path))
  (fs/writeFileSync logfile-path)
  nil)

(defn log [text-to-log]
  (fs/writeFileSync logfile-path text-to-log))