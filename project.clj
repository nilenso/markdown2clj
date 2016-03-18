(defproject markdown2clj "0.1.0"
  :description "A librabry to convert markdown string to clojure data-structure"
  :url "http://nilenso.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.atlassian.commonmark/commonmark "0.4.1"]
                 [com.atlassian.commonmark/commonmark-ext-gfm-tables "0.4.1"]]
  :main markdown2clj.core)
