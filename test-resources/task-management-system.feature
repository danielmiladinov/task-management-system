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
