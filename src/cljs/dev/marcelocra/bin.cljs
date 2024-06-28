(ns dev.marcelocra.bin
  (:require
  ;;  [dev.marcelocra.lib :as lib]
  ;;  ["shelljs$default" :as sh]
   ["fs" :as fs]
   ["path" :as path]))


(println "Hello from bin.cljs. Now it is:"
         (.toLocaleString (js/Date.) "pt-BR"))


(defonce ^:dynamic *args*
  (let [args js/process.argv
        args# (count args)]
    (cond
      (< 2 args#) (throw (js/Error. "No arguments passed to this script."))
      (= 2 args#) []
      :else (-> args
                (.slice 2)
                js->clj))))


(def source-path (path/resolve "src" "cljs" "dev" "marcelocra"))
(def templates-folder-path (path/resolve source-path "templates"))


;; playground!
(comment

  (println (fs/readdirSync templates-folder-path))

  (binding [*args* ["arg1" "arg2" "arg3"]]
    (println *args*))

  #_("do not wrap me!"))


(defn ^:export main []
  (println "These are the arguments passed to this script:" *args*))
