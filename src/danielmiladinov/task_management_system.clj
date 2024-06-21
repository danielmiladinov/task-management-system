(ns danielmiladinov.task-management-system
  (:gen-class))

(def ^:private tasks (atom []))

(defn set-tasks
  [new-tasks]
  (reset! tasks new-tasks))

(defn add-task
  [new-task]
  (swap! tasks conj new-task))

(defn set-task-status
  [task-title new-status]
  (swap! tasks (partial map (fn [{:keys [title] :as task}]
                              (cond-> task
                                      (= title task-title)
                                      (assoc :status new-status))))))

(defn by-priority-and-status
  "Return a collection of the tasks in the system in descending priority and status order"
  []
  (sort-by (juxt (comp {:high 1 :medium 2 :low 3}
                       :priority)
                 (comp {:in-progress 1 :pending 2 :completed 3}
                       :status))
           @tasks))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))
