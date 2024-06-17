#!/usr/bin/env nbb
(ns dev.marcelocra.sync-repo.core
  (:require
   [babashka.cli :as cli]
   ["path" :as path]
   ["fs" :as fs]
   [clojure.string :as s]
   [clojure.repl :refer [doc find-doc]]
   [goog.string :as gstring]))

(def ^:dynamic *debug* "Prevents stuff from actually happening during development." true)

(def logfile-path (-> js/process.env
                      (.-HOME)
                      (path/resolve ".sync-repo.log.txt")))

(defn log [text-to-log]
  (if *debug*
    text-to-log
    (fs/writeFileSync logfile-path text-to-log)))

(defn create-logfile
  "Creates the log file if it doesn't already exists."
  []
  (if (not (fs/existsSync logfile-path))
    (do
      (log "logfile doesn't exist. creating...")
      (fs/writeFileSync logfile-path)
      (log "done!"))
    nil))

(defn dump-log
  "Dumps the whole content of the logfile as it currently is."
  []
  (fs/readFileSync logfile-path "utf8"))

(defn pad-start
  "Always produces the number with 2 digits, the first being a zero when there's only one.

   E.g.:
       5 -> 05
      10 -> 10
       1 -> 01
   "
  [^js/number number]
  (.padStart (str number) 2 "0"))



(defn date-format
  "Creates a string from a Date object, formatted as 'yyyy-MM-DD_HH-MM-SS'."
  [^js/Date date]
  (let [yyyy (.getFullYear date)
        MM (pad-start (.getMonth date))
        DD (pad-start (.getDate date))
        HH (pad-start (.getHours date))
        mm (pad-start (.getMinutes date))
        ss (pad-start (.getSeconds date))]
    (gstring/format "%s-%s-%s_%s-%s-%s" yyyy MM DD HH mm ss)))

(defn date-locale-format
  "Creates a locale formatted string from a Date object. Uses 'en-UK' as the default locale if one is not given."
  ([^js/Date date]
   (date-locale-format date "en-UK"))
  ([^js/Date date locale]
   (.toLocaleString date locale #js{:day "numeric",
                                   ;; :weekday "short", => Mon
                                   ;; :month "short", ;; => Jun
                                    :month "2-digit",  ;; => 06
                                    :year "numeric",
                                    :hour "2-digit",
                                    :hour12 false,
                                    :minute "2-digit",
                                    :second "2-digit",
                                    :timeZone "America/Sao_Paulo"})))

(defn now []
  (date-format (js/Date.)))

(def cli-spec
  {:spec
   {:num {:coerce :long
          :desc "Number of some items"
          :alias :n                     ; adds -n alias for --num
          :validate pos?                ; tests if supplied --num >0
          :require true}                ; --num,-n is required
    :flag {:coerce :boolean             ; defines a boolean flag
           :desc "I am just a flag"}}
   :error-fn                           ; a function to handle errors
   (fn [{:keys [spec type cause msg option] :as data}]
     (if (= :org.babashka/cli type)
       (case cause
         :require
         (println
          (gstring/format "Missing required argument: %s\n" option))
         :validate
         (println
          (gstring/format "%s does not exist!\n" msg)))))})

(defn show-help
  [spec]
  (cli/format-opts (merge spec {:order (vec (keys (:spec spec)))})))

(defn -main [args]
  (let [opts (cli/parse-opts args cli-spec)]
    (if (or (:help opts) (:h opts))
      (println (show-help cli-spec))
      (println "your args: " opts)))
  (binding [*debug* true]
    (create-logfile)
    (log "\n")
    (log (now))))

(-main *command-line-args*)