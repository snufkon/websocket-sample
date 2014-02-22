(defproject websocket-sample "0.1.0-SNAPSHOT"
  :description "WebSocket sample program of Clojure"
  :url "https://github.com/snufkon/websocket-sample"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [compojure "1.1.6"]
                 [ring "1.2.1"]
                 [domina "1.0.2"]
                 [http-kit "2.1.17"]
                 [com.taoensso/timbre "3.0.1"]
                 [environ "0.4.0"]]
  :plugins [[lein-cljsbuild "1.0.0"]
            [lein-environ "0.4.0"]]
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/main.js"
                           :optimizations :whitespace
                           :pretty-print true}}]}
  :profiles {:production {:env {:production true}}}
  :main websocket-sample.handler)
