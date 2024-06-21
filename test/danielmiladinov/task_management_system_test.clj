(ns danielmiladinov.task-management-system-test
  (:require [burpless :refer [run-cucumber step]]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [danielmiladinov.task-management-system :as tms])
  (:import (clojure.lang IObj)
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
           (reset! tms/tasks (->> (.asMaps dataTable)
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
           state))])

(deftest task-management-system-feature
  (is (zero? (run-cucumber "test-resources/task-management-system.feature" steps))))
