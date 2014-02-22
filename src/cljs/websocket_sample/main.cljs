(ns websocket-sample.main
  (:require [domina :as dom]
            [domina.events :as events]
            [cljs.reader :as reader]))

(def ws-url "ws://localhost:8080/chat")
(def ws (js/WebSocket. ws-url))

(defn send [evt]
  (events/prevent-default evt)
  (let [msg (-> (dom/by-id "msg") (dom/value))
        name (-> (dom/by-id "name") (dom/value))]
    (.send ws {:name name :msg msg})
    (dom/set-value! (dom/by-id "msg") "")))

(defn extract [data]
  (-> (.-data data)
      (reader/read-string)))

(defn message [data]
  (let [name (:name data)
        msg (:msg data)]
    (str "<li>" name ": " msg "</li>")))

(defn receive [data]
  (let [data (extract data)]
    (dom/append! (dom/by-id "logs") (message data))))

(defn init []
  (events/listen! (dom/by-id "myForm") :submit send)
  (set! (.-onmessage ws) receive))

(set! (.-onload js/window) init)
