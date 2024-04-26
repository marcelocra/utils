#!/usr/bin/env bb
;; vim: autoindent expandtab tabstop=2 shiftwidth=2
;;
;; Command line utils.
(ns core
  (:require
    [clojure.java.shell :refer [sh]]
    [clojure.string :as s]
    [babashka.fs :as fs]
    [babashka.cli :as cli]
    [clojure.java.io :as io]
    [clojure.repl :refer [doc]]
    [clojure.pprint :as pp :refer [pprint] :rename {pprint p}]))

(def debug false)
(def projects-dir (System/getenv "MCRA_PROJECTS_FOLDER"))
(def templates-for-new-projects ["astro" "react" "deno" "bun" "clj" "cljs"])
(defn js-package-mananger-note []
  (println (->> ["Default Nodejs package manager: `pnpm`"
                 ""
                 "You can change that by providing the `js-package-manager` option"
                 "in the command line. Use whichever you prever, but commands were"
                 "only tested with `pnpm`."
                 ""
                 "We print the command we are executing, so you can try to adapt it to use"
                 "with your preferred package manager."
                 ""
                 "Good luck!"
                 ""
                 "(p.s.: if you don't have a preferred package manager, try `pnpm`."
                 " It is very good!)"]
                (s/join "\n"))))

(if (nil? (System/getenv "MCRA_CLI_UTILS_PATH"))
  (do
    (println "Please, define MCRA_CLI_UTILS_PATH env variable and try again.")
    (println "It should point to the `utils` script from the `dev` project.")
    (System/exit 1))
  nil)

(def templates-dir
  (-> (System/getenv "MCRA_CLI_UTILS_PATH")
      (fs/parent)
      (fs/file)
      (io/file "templates")
      (.toString)

      ;; When you don't know the api, you end up coding more. For the same
      ;; thing above, initially I wrote the code below (which wasn't working,
      ;; btw).

      ; (io/file)
      ; (fs/components)
      ; (->> (map #(.toString %)))
      ; (drop-last)
      ; (reverse)
      ; (conj "templates")
      ; (reverse)
      ; (->> (apply io/file))
      ; (fs/absolutize)
      ; (.toString))
      ))

(comment
  (-> (fs/list-dir templates-dir)
      (->> (map #(.toString %)))
      (->> (s/join "\n"))
      (println))

  ;; rcf - rich comment form
  )

(defn produce-file-path
  [name]
  (-> templates-dir
      (io/file name)
      (.toString)))

(def file-names
  "IMPORTANT: update the `template-files` too."
  {:bb "bb.edn"
   :prettier ".prettierrc.json"
   :shadow-cljs "shadow-cljs.edn"
   :build "build.clj"})

(def template-files
  {:bb (produce-file-path (:bb file-names))
   :prettier {"js" (produce-file-path (:prettier file-names))}
   :shadow-cljs (produce-file-path (:shadow-cljs file-names))
   :build (produce-file-path (:build file-names))})

(defn file-to-write
  "Prepares a filename for the given name in the current path."
  [name]
  (-> (fs/cwd)
      (.toString)
      (io/file name)
      (.toString)))


;; -----------------------------------------------------------------------------
;; CLI helper

(def cli-options
  {:lang {:default "js"}
   :fix {:default false}
   :proj {:default nil}
   :name {:default nil}
   :js-package-manager {:default "pnpm"}})

;; Made dynamic to simplify testing.
(def ^:dynamic args
  (cli/parse-opts *command-line-args* {:spec cli-options}))

(def cmd-str (first *command-line-args*))


;; -----------------------------------------------------------------------------

(defn free-ram
  "Prints the amount of free ram memory."
  []
  (println
    (->>
      (:out (sh "cat" "/proc/meminfo"))
      (re-find #"MemFree.*")
      (re-find #"[0-9]+.*"))))


;; -----------------------------------------------------------------------------

(defn pretty
  "Creates a prettierrc file for one of the following languages (use the options from the parenthesis): JavaScript (default or :js)."
  []
  (let [file-name (file-to-write (:prettier file-names))]
    (if (fs/exists? file-name)
      (println (format "File '%s' exists. Aborting." file-name))
      (let [lang (:lang args)
            content (fs/read-all-bytes (get-in template-files [:prettier lang] (get-in template-files [:prettier "js"])))]
        (if (nil? content)
          (println (str "Option '" lang "' not available. See help."))
          (fs/write-bytes file-name content))))))


;; -----------------------------------------------------------------------------

(defn time-in
  "Returns the current time in the given timezone or America/New_York (default)."
  []
  (let [tz (get args :tz "America/New_York")
        now (java.time.ZonedDateTime/now)
        timezone (java.time.ZoneId/of tz)
        tz-time (.withZoneSameInstant now timezone)
        pattern (java.time.format.DateTimeFormatter/ofPattern "HH:mm")]
    (println (format "[%s] %s" tz (.format tz-time pattern)))))

;; Example on how to create a binding to test functions.
(comment
  (binding [args {:tz "America/Toronto"}]
    (time-in))

  ;; rcf - rich comment form
  )


;; -----------------------------------------------------------------------------

(defn bb
  "Creates a new bb.edn file in the current directory."
  []
  (let [file-name (file-to-write (:bb file-names))]
    (if (fs/exists? file-name)
      (println (format "File '%s' exists. Aborting." file-name))
      (let [lang (:lang args)
            content (fs/read-all-bytes (get template-files :bb))]
        (if (nil? content)
          (println (str "Option '" lang "' not available. See help."))
          (fs/write-bytes file-name content))))))


;; -----------------------------------------------------------------------------
(defn notify
  "Displays the resulting output of the command as a desktop.... well... that
  won't work hahahahahaha. We need to do as follows:

  - if notifications are currently enabled, we say that and mention that we will
  disable them
  - if notifications are disabled, we enable them and notify the user that they
  were enabled

  The command with possible options is:

  notify-send [options] {summary} body

  Options:
  --urgency={low,normal,critical} 
  --expire-time={millis}
  --icon={filename,stock icon}
  --category={type,type,...}      ;; optional, but not sure about it
  --hint=type:name:value          ;; same as above

  More documentation here: https://galago-project.org/specs/notification/0.9/index.html"
  [text & {:keys [expire-time]
           :or {expire-time 3000}}]
  (sh "notify-send" (str "--expire-time=" expire-time) "Notification status" text))

(defn toggle-notifications
  "Enables/disables desktop notifications in an ubuntu/mint with cinnamon de."
  []
  (let [enabled-status "true\n"
        notifications-enabled?  (= enabled-status (:out (sh "gsettings"
                                                            "get"
                                                            "org.cinnamon.desktop.notifications"
                                                            "display-notifications")))
        _ (and notifications-enabled? 
               (notify (str
                         "About to disable notifications... won't be able to "
                         "notify you about the result, of course haha")))]
    (let [result (sh "gsettings"
                     "set"
                     "org.cinnamon.desktop.notifications"
                     "display-notifications"
                     (str (not notifications-enabled?)))
          current-status (:out (sh "gsettings"
                                   "get"
                                   "org.cinnamon.desktop.notifications"
                                   "display-notifications"))
          currently-enabled? (= current-status enabled-status)]
      (if currently-enabled?
        (do
          (println "Notifications enabled!")
          (notify "Notifications enabled!"))
        (println "Notifications disabled!")))))


;; -----------------------------------------------------------------------------
(defn create-file-from-template
  "Creates a file with `file-name` from the `template` in the current directory, if the file doesn't already exists. If it does, shows a message and aborts."
  [file-name template]
  (if (fs/exists? file-name)
    (println (format "File '%s' exists. Aborting." file-name))
    (let [content (fs/read-all-bytes (produce-file-path (or template file-name)))]
      (if debug
        (println (str "DEBUG mode:\ncreate file: '" file-name "'"
                      "\nwith content (between lines):\n------------------------------------\n"
                      (String. content)
                      "------------------------------------"))
        (fs/write-bytes file-name content)))))

(defn shadow
  "Creates a shadow-cljs.edn file and a .gitignore in the current directory."
  []
  (doseq [{:keys [file-name template]} [{:file-name "shadow-cljs.edn"}
                                        {:file-name ".gitignore"
                                         :template "shadow-cljs.gitignore"}]]
    (create-file-from-template file-name template)))


;; -----------------------------------------------------------------------------
(defn build
  "Creates a build.clj file in the current directory, to help running commands."
  []
  (let [template :build
        file-name (file-to-write (template file-names))]
    (if (fs/exists? file-name)
      (println (format "File '%s' exists. Aborting." file-name))
      (let [content (fs/read-all-bytes (get template-files template))]
        (fs/write-bytes file-name content)))))


;; -----------------------------------------------------------------------------
(defn fmt
  "Formats Clojure files using cljfmt."
  []
  (println 
    (apply sh (-> 
                "clj -Tcljfmt %s" 
                (format (if (:fix args) "fix" "check"))
                (s/split #" ")))))


;; -----------------------------------------------------------------------------
(defn numbered-projs []
  (let [projs (-> projects-dir
                  (fs/list-dir)
                  (->> 
                    (filter #(fs/directory? %))
                    (into (sorted-set-by (fn [a b] (.compareTo 
                                                     (fs/last-modified-time b) 
                                                     (fs/last-modified-time a)))))
                    (map #(s/replace % (re-pattern (str projects-dir "/")) ""))
                    #_(s/join "\n")))
        ids (range 0 (count projs))]
    (into (sorted-map) (zipmap ids projs))))

(defn code
  "Opens VSCode with the given project."
  []
  (let [proj (:proj args)] 
    (if (or (nil? proj) (boolean? proj))
      (do
        (println "Choose one of the projects below, by name or number.")
        (println (format "They are in '%s':" projects-dir))
        (p (numbered-projs)))
      (let [num-to-proj (numbered-projs)
            proj-dir (io/file projects-dir (if (int? proj) 
                                             (get num-to-proj proj (get num-to-proj 0))
                                             proj))]
        (sh "code" "." :dir proj-dir)))))

(comment
  ;; print all projects
  (binding [args {:proj nil}]
    (code))

  ;; open the first one
  (binding [args {:proj 0}]
    (code))

  ;; open some in the middle
  (binding [args {:proj 5}]
    (code))

  ;; open the last one
  (binding [args {:proj 27}]
    (code))

  ;; blow it up!
  (binding [args {:proj 28}]
    (code))

  ;; rcf - rich comment form
  )


;; -----------------------------------------------------------------------------
(defn git-orphan
  "Creates a branch with no history in the current repo."
  []
  (let [name (:name args)] 
    (if (nil? name)
      (do
        (println "Please, provide the :name argument with a name for the new branch.")
        (System/exit 1))
      (:out (sh "git" "switch" "--orphan" name)))))


;; -----------------------------------------------------------------------------
(defn nvmrc
  "Creates a .nvmrc file in the current dir."
  []
  (doseq [{:keys [file-name template]} [{:file-name ".nvmrc"}]]
    (create-file-from-template file-name template)))


;; -----------------------------------------------------------------------------
(defn n
  "Creates a new project following one of the available templates."
  []
  (let [_ (js-package-mananger-note)
        name (:name args)
        usage (s/join 
                "\n- "
                (concat
                  ["Please, provide the :name argument, choosing one of the following templates:"]
                  templates-for-new-projects))
        display-usage-and-exit (do (println usage)
                                   (System/exit 1))]
    (if (nil? name)
      (do
        (p name)
        (display-usage-and-exit))
      (let [options {"astro" "pnpm create astro@latest"
                     "react" "echo 'not ready'"
                     "deno" "echo 'not ready'"
                     "bun" "echo 'not ready'"
                     "clj" "echo 'not ready'"
                     "cljs" "echo 'not ready'"}
            selected-option (get options name nil)]
        (if (nil? selected-option) 
          (display-usage-and-exit) 
          (let [cmd (s/split selected-option #" ")]
            (println cmd)
            (:out (sh cmd))))))))


;; -----------------------------------------------------------------------------
(defn login-history
  "Lists the login history, along with some other stuff like sudo calls. (For Ubuntu-based distros. Tested in Linux Mint.)"
  []
  (println "vim /var/log/auth.log"))


;; -----------------------------------------------------------------------------
;; Next command here.


;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------
;; NO COMMANDS BELOW HERE.
;;
;; This section is meant for the helper function and code that runs the other
;; commands.

(defn help []
  (println
    (str "Choose one of the commands below: \n\n"
         (s/join "\n"
                 (map #(format "%-25s%s" (:name %) (:doc %))
                      [(meta #'free-ram)
                       (meta #'pretty)
                       (meta #'time-in)
                       (meta #'bb)
                       (meta #'toggle-notifications)
                       (meta #'shadow)
                       (meta #'fmt)
                       (meta #'code)
                       (meta #'git-orphan)
                       (meta #'nvmrc)
                       (meta #'n)
                       (meta #'login-history)
                       ])))))


;; Run the selected command.
(if (nil? cmd-str)
  (help)
  (let [fn-to-run (resolve (symbol cmd-str))]
    (if (nil? fn-to-run)
      (help)
      (fn-to-run))))


;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------
;; PLAYGROUND!! :)

(comment
  (def templates-dir (-> (fs/cwd)
                         (.toString)
                         (io/file "src" "templates")
                         (.toString)))

  ;; creates one file for each of the templates
  (->
    templates-dir
    (io/file "bb.edn")
    (fs/write-bytes (.getBytes "here goes the desired file content")))

  (->
    templates-dir
    (io/file ".prettierrc.json")
    (fs/write-bytes (.getBytes "here goes the desired file content")))

  ;; creates a dir for templates if it doesn't exists and/or returns its content
  (->
    templates-dir
    (fs/create-dirs)
    (fs/list-dir))



  (-> (fs/cwd)
      (.toString))

  ;; rcf - rich comment form
  )

