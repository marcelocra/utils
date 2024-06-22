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


;; ---------------------------------------------------------------------------------------------------------------------
;; ----------- PLAYGROUND ARCHIVES :) ----------------------------------------------------------------------------------
;; ---------------------------------------------------------------------------------------------------------------------

;; changing the color scheme from light/dark/light
(comment
  (re-find #"colorTheme.*Dark" settings-content)

  (->>
   (reflect/reflect java.io.InputStream)
   :members
   (sort-by :name)
   (pp/print-table))

  (pprint (set (map #(.getName %) (vec (.getMethods (.getClass settings-path))))))
  (-> settings-path
      (.getClass)
      (.getFields))

  (type settings-path)
  (ancestors (type settings-path))

  (pprint (map #(.getName %) (-> settings-path class .getMethods)))

  ;; rcf
  )

;; changing settings colors based on an edn comment
(comment
  ;; read file if necessary
  #_(def settings-content (slurp settings-path))

  ;; to allow playing without actually nuking the file content
  (defonce backup-settings-content settings-content)
  (defn reset-settings []
    (s/split settings-path backup-settings-content))

  ;; if this line matches, potentially that there are colors to be changed
  (def matches (re-seq #"(.*)//(\{:dark.*)" settings-content))
  (map #(pprint (edn/read-string (get % 2)))
       (re-seq #"(.*)//(\{:dark.*)" settings-content))
  (count matches)
  (first matches)
  (rest matches)
  (type matches)
  (count (next matches))
  (type (next matches))
  (type (rest matches))

  (defn p [stuff]
    (pprint stuff))

  #_(spit settings-path backup-settings-content)
  #_(spit settings-path (get @new-contents (- (count @new-contents) 1)))
  #_(spit settings-path (new-file-content (slurp settings-path)))


  ;; checking file contents

  (def atom-content {:id 1})
  (defonce new-contents (atom atom-content))
  (reset! new-contents atom-content)
  (do
    (swap! new-contents update :id inc)
    #_(swap! new-contents assoc (:id @new-contents) (new-file-content)))
  @new-contents
  (count @new-contents)

  ;; rcf - rich comment form
  )


;; Consider create categories one day, to group settings that should be changed
;; together, like colors, fonts, etc.
;;
;; The code below is a draft of how it could be done.
(comment
  (def options-as-map
    "Gets all settings that have options in an edn comment.
  
   For the comment to be parsed, it must be formatted like below:
   
   // edn{...}

   NOTE: the space between `//` and `edn{...}` matters.
   
   TODO: Returns a map in the form:

   {:category :themes
    :key \"editor.lineHightlightBorder\"
    :current-value \"#00ffff22\"
    :options {:light \"#00ff00\" :dark \"#ff0000\"}}
   "
    (reduce
     #(assoc
       %1
       #_(read-string (format ":category "))
       (read-string (format "\"%s\"" (get %2 1)))
       (read-string (format "{:current %s :options %s}" (get %2 2) (get %2 3))))
     {}
   ;; First capture: the setting key, e.g. "editor.fontSize".
   ;; Second capture: the setting value, e.g. 15 (it respects the types).
   ;; Third capture: the settings options from the edn data in a comment.
     (re-seq #".*\"(.*)\": (.*),.*// edn(\{.*\})" settings-content)))

  (def light-themes
    {:options ["Quiet Light"
               "Solarized Light"]
     :preferred "Solarized Light"})

  (def dark-themes
    {:options ["Abyss"
               "Default Dark+"
               "GitHub Dark"
               "GitHub Dark Default"
               "GitHub Dark Dimmed"
               "Monokai"
               "Monokai Dimmed"
               "Solarized Dark"]
     :preferred "Solarized Dark"})

  (defn find-current-theme []
    (second (re-find #".*\"workbench\.colorTheme\": \"(.*)\".*" settings-content)))

  :rcf)
