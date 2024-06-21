(ns danielmiladinov.task-management-system-test
  (:require [burpless :refer [run-cucumber step]]
            [clojure.test :refer [deftest is]]
            [danielmiladinov.task-management-system :as tms])
  (:import (io.cucumber.datatable DataTable)
           (io.cucumber.docstring DocString)
           (io.cucumber.java PendingException)))

(def steps
  [(step :Given "I have the following tasks:"
         ^:datatable
         (fn [state ^DataTable dataTable]
           (throw (PendingException.))))

   (step :When "I list my tasks by priority and status"
         (fn [state]
           (throw (PendingException.))))

   (step :Then "I should see the following tasks:"
         ^:docstring
         (fn [state ^DocString docString]
           (throw (PendingException.))))])

(deftest task-management-system-feature
  (is (zero? (run-cucumber "test-resources/task-management-system.feature" steps))))
