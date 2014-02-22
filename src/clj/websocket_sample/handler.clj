(ns websocket-sample.handler
  (:require [org.httpkit.server :as httpkit]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :as reload]
            [ring.util.response :refer [file-response]]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [taoensso.timbre :refer [info]]))

(def channels (atom []))

(defn send-to-clients [data]
  (doseq [c @channels] (httpkit/send! c data))
  (info "send to" (count @channels) "clients."))

(defn receive [data]
  (info "receive: " data)
  (let [m (edn/read-string data)
        name (if (clojure.string/blank? (:name m))
               "Anonymous"
               (:name m))
        msg (:msg m)]
    (if (not (clojure.string/blank? msg))
      (send-to-clients (pr-str {:name name :msg msg})))))

(defn add-channel [channel]
  (if (not-any? #{channel} @channels)
    (swap! channels #(conj %1 channel))))

(defn chat-handler [request]
  (httpkit/with-channel
   request
   channel
   (add-channel channel)
   (httpkit/on-receive channel receive)))

(defroutes app-routes
  (GET "/" [] (file-response "index.html" {:root "resources/public"}))
  (GET "/chat" [] chat-handler)
  (route/resources "/")
  (route/not-found "<h1>Page Not Found</h1>"))

(def app (if (env :production)
           (handler/site #'app-routes)
           (do
             (info "add code reload middleware")
             (-> (handler/site #'app-routes)
               (reload/wrap-reload)))))

(defn -main [& args]
  (httpkit/run-server app {:port 8080}))
