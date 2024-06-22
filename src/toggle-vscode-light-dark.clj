#!/usr/bin/env bb

(require
 '[clojure.string :as s]
 '[clojure.java.io :as io]
 '[babashka.fs :as fs]
 '[clojure.pprint :as pp :refer [pprint]]
 '[clojure.reflect :as reflect]
 '[clojure.edn :as edn])

;; This works on Linux, but might not work on other systems.
(def settings-path (-> (System/getenv "HOME")
                       (io/file ".config" "Code" "User" "settings.json")
                       (fs/canonicalize)  ;; Necessary to follow symlinks (`slurp` doesn't).
                       (.toString)))

(def settings-content (slurp settings-path))

;; Check some settings lines.
(comment
  (map
   #(pprint (edn/read-string (get % 1)))
   (re-seq #".*(\{:dark.*)" settings-content))

  ;; rcf - rich comment form
  )

;; TODO: add more options and allow choosing from cmd line.
(def theme-mapper
  {:dimmed {:dark "\"GitHub Dark Dimmed\""
            :light "\"Solarized Light\""}
   :abyss {:dark "\"Abyss\""
           :light "\"Quiet Light\""}
   :github-dark-default {:dark "\"GitHub Dark Default\""
                         :light "\"Solarized Light\""}
   :solarized {:dark "\"Solarized Dark\""
               :light "\"Solarized Light\""}})

(defn get-theme []
  (let [current (:dimmed theme-mapper)]
    [(:light current) (:dark current)]))

(defn update-colors [settings-content]
  (let [matches (re-seq #"(.*)// edn(\{:dark.*\}).*" settings-content)]
    (loop [not-processed matches
           current-content settings-content
           abort-counter 1]
      (if (or
           (> abort-counter 1000)
           (<= (count not-processed) 0))
        current-content
        (let [currently-processing (first not-processed)
              opts (edn/read-string (get currently-processing 2))
              line-content-to-replace (get currently-processing 1)
              is-light? (not (nil? (re-find (re-pattern (:light opts)) line-content-to-replace)))
              replace-with (s/replace line-content-to-replace
                                      (re-pattern (if is-light? (:light opts) (:dark opts)))
                                      (if is-light? (:dark opts) (:light opts)))
              replaced-content (s/replace current-content
                                          line-content-to-replace
                                          replace-with)]
          (recur (next not-processed)
                 replaced-content
                 (inc abort-counter)))))))

(defn from-theme-to-theme [from to]
  (println
   (format "from '%s' to '%s'" from to))
  (spit settings-path
        (s/replace
         (update-colors settings-content)
         (re-pattern from)
         to)))

(comment
  (let [[light-theme _] (get-theme)
        light-theme-str-in-settings? (re-find (re-pattern (str "colorTheme.*" light-theme)) settings-content)
        ;; light-theme-str-in-settings? (not (nil? (re-find (re-pattern (str "colorTheme.*" light-theme)))))
        ]
    light-theme-str-in-settings?)

  :rcf)

(defn toggle
  "If light mode is enabled, toggle dark mode (and vice-versa)."
  []
  (let [[light-theme dark-theme] (get-theme)
        light-theme-str-in-settings? (not (nil? (re-find (re-pattern (str "colorTheme.*" light-theme)) settings-content)))]
    (if light-theme-str-in-settings?
      (from-theme-to-theme light-theme dark-theme)
      (from-theme-to-theme dark-theme light-theme))))

(toggle)
