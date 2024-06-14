#!/usr/bin/env nbb
(ns dev.marcelocra.sync-repo.core
  (:require
   ["path" :as path]
   ["fs" :as fs]))

(def debug true)

(def logfile-path (-> js/process.env
                      (.-HOME)
                      (path/resolve ".sync-repo.log.txt")))

(if (not (fs/existsSync logfile-path))
  (fs/writeFileSync logfile-path)
  nil)

(defn log [text-to-log]
  (if debug
    (println text-to-log)
    (fs/writeFileSync logfile-path text-to-log)))

(defn dump-log []
  (fs/readFileSync logfile-path "utf8"))

(defn now []
  (-> (js/Date.)
      (.toLocaleString "en-UK" #js{:day "numeric",
                                   :weekday "short",
                                   :month "short",
                                   :year "numeric",
                                   :hour "2-digit",
                                   :hour12 false,
                                   :minute "2-digit",
                                   :second "2-digit",
                                   :timeZone "America/Sao_Paulo"})))

(log "\n")
(log (now))
