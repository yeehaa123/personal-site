(set-env!
 :resource-paths #{"src" "html"}
 :dependencies '[[adzerk/boot-cljs            "1.7.228-1"      :scope "test"]
                 [adzerk/boot-cljs-repl       "0.3.3"          :scope "test"]
                 [adzerk/boot-reload          "0.4.12"          :scope "test"]
                 [pandeiro/boot-http          "0.7.3" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.0-SNAPSHOT" :scope "test"]
                 [org.clojure/clojure         "1.9.0-alpha10"]
                 [org.clojure/clojurescript   "1.9.76"]
                 [com.cemerick/piggieback     "0.2.2-SNAPSHOT"          :scope "test"]
                 [weasel                      "0.7.0"          :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12"         :scope "test"]
                 [rum "0.10.5"]
                 [cljsjs/moment "2.10.6-4"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[crisptrutski.boot-cljs-test  :refer [exit! test-cljs]]
 '[pandeiro.boot-http    :refer [serve]])

(deftask testing []
  (merge-env! :resource-paths #{"test"})
  identity)

(deftask auto-test []
  (comp (testing)
        (watch)
        (speak)
        (test-cljs)))

(deftask dev []
  (task-options! target {:dir #{"dev/"}})
  (comp (serve :dir "dev/")
        (watch)
        (speak)
        (reload :on-jsload 'app.core/main)
        (cljs-repl)
        (cljs :source-map true :optimizations :none)
        (target)))

(deftask test []
  (comp (testing)
        (test-cljs)
        (exit!)))

(deftask build []
  (task-options! target {:dir #{"dist/"}})
  (comp (cljs :optimizations :advanced)
        (target)))
