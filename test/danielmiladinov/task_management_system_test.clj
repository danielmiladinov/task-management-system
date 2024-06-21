(ns danielmiladinov.task-management-system-test
  (:require [burpless :refer [run-cucumber step]]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [danielmiladinov.task-management-system :as tms])
  (:import (clojure.lang IObj Keyword)
           (io.cucumber.datatable DataTable)))

(def to-keyword
  "Turns e.g. “In Progress” into :in-progress"
  (comp keyword
        #(str/replace % #"\s" "-")
        str/lower-case))

(def steps
  [(step :Given "I have the following tasks:"
         ^:datatable
         (fn [state ^DataTable dataTable]
           (tms/set-tasks (->> (.asMaps dataTable)
                               (map (fn [m]
                                      (-> (update-keys m to-keyword)
                                          (update :status to-keyword)
                                          (update :priority to-keyword))))))
           state))

   (step :When "I list my tasks by priority and status"
         (fn [state]
           (assoc state :actual-tasks (tms/by-priority-and-status))))

   (step :Then "I should see the following tasks:"
         ^{:docstring IObj}
         (fn [{:keys [actual-tasks] :as state} ^IObj expected-tasks]
           (assert (= expected-tasks actual-tasks))
           state))

   (step :When "I add a new task with title {string}, description {string}, with priority {keyword}"
         (fn [state ^String title ^String description ^Keyword priority]
           (tms/add-task {:title title :description description :priority priority :status :pending})
           state))

   (step :Then "the task {string} should appear in my list of tasks, with a status of {keyword}"
         (fn [state ^String title ^Keyword status]
           (let [matching-task (->> (tms/by-priority-and-status)
                                    (filter (every-pred (comp (hash-set title) :title)
                                                        (comp (hash-set status) :status)))
                                    first)]
             (assert (some? matching-task) (format "Did not find task with title %s and status %s" title status))
             state)))

   (step :Then "the total number of tasks should be {int}"
         (fn [state ^Integer expected-number-of-tasks]
           (let [actual-number-of-tasks (count (tms/by-priority-and-status))]
             (assert (= expected-number-of-tasks actual-number-of-tasks)
                     (format "Expected to find %d task%s, but actually found %d task%s"
                             expected-number-of-tasks
                             (if (= 1 expected-number-of-tasks) "" "s")
                             actual-number-of-tasks
                             (if (= 1 actual-number-of-tasks) "" "s"))))
           state))

   (step :When "I mark the task {string} as {keyword}"
         (fn [state ^String title ^Keyword status]
           (tms/set-task-status title status)
           state))

   (step :When "I edit the task {string} to change the priority to {keyword}"
         (fn [state ^String title ^Keyword priority]
           (tms/set-task-priority title priority)
           state))

   (step :Then "the task {string} should have the priority {keyword}"
         (fn [state ^String title ^Keyword priority]
           (let [matching-task (->> (tms/by-priority-and-status)
                                    (filter (every-pred (comp (hash-set title) :title)
                                                        (comp (hash-set priority) :priority)))
                                    first)]
             (assert (some? matching-task) (format "Did not find task with title %s and priority %s" title priority))
             state)))

   (step :When "I delete the task {string}"
         (fn [state ^String title]
           (tms/delete-task title)
           state))

   (step :Then "the task {string} should be removed from my list of tasks"
         (fn [state ^String title]
           (let [no-such-task (->> (tms/by-priority-and-status)
                                   (filter (comp (hash-set title) :title))
                                   first)]
             (assert (nil? no-such-task) (format "Expected to not find a task with title %s, but did" title))
             state)))])

(deftest task-management-system-feature
  (is (zero? (run-cucumber "test-resources/task-management-system.feature" steps))))
