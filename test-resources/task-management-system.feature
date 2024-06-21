Feature: Task Management System

  As a user, I want to manage my tasks, so that I can keep track of my work and priorities.

  Background:
    Given I have the following tasks:
      | title         | description                | priority | status      |
      | Finish report | Complete annual report     | High     | Pending     |
      | Team meeting  | Monthly team sync          | Medium   | Completed   |
      | Plan project  | Outline new project        | Low      | In Progress |
      | Buy groceries | Buy groceries for the week | Medium   | Pending     |

  Scenario: Listing tasks
    When I list my tasks by priority and status
    Then I should see the following tasks:
    """edn
    [{:title "Finish report" :description "Complete annual report"     :priority :high    :status :pending}
     {:title "Buy groceries" :description "Buy groceries for the week" :priority :medium  :status :pending}
     {:title "Team meeting"  :description "Monthly team sync"          :priority :medium  :status :completed}
     {:title "Plan project"  :description "Outline new project"        :priority :low     :status :in-progress}]
    """

  Scenario: Adding more tasks
    When I add a new task with title "Read book", description "Read the new book I bought", with priority :low
    Then the task "Read book" should appear in my list of tasks, with a status of :pending
    And the total number of tasks should be 5
    When I add a new task with title "Publish blog post", description "Clojure and burpless for great success!", with priority :high"
    Then the task "Publish blog post" should appear in my list of tasks, with a status of :pending
    And the total number of tasks should be 6
    When I add a new task with title "Pay bills", description "Cell, electric, and internet", with priority :medium
    Then the task "Pay bills" should appear in my list of tasks, with a status of :pending
    And the total number of tasks should be 7

  Scenario: Starting a task
    When I mark the task "Buy groceries" as :in-progress
    Then the task "Buy groceries" should appear in my list of tasks, with a status of :in-progress

  Scenario: Completing a task
    When I mark the task "Finish report" as :completed
    Then the task "Finish report" should appear in my list of tasks, with a status of :completed

  Scenario: Changing priority
    When I edit the task "Plan project" to change the priority to :high
    Then the task "Plan project" should have the priority :high
