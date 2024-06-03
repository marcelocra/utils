#!/usr/bin/env nbb

(ns core
  (:require
   ["bun" :refer ($)]
   ["fs" :as fs]))

(println 'hey)

($ "echo hey")

(fs/readdirSync ".")