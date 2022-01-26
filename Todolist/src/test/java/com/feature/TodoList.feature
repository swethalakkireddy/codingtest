@manageTodoList
Feature: Manage To do List
  As a Lead of a team I wanted to manage my daily todo list

  Background: User navigates to todolist page and adds tasks to todo list
    Given I hit URL "https://todomvc.com/examples/vue/#/all"
    And I see "TodoMVC" page
    When I enter a todo task
      | open restaurant at 9 |
      | check inventory |
      | collect orders |
      | serve orders |
    Then I observe the entries of todolist in "All" filter
    And I observe "All" filter is displayed
    And I observe "Completed" filter is displayed
    And I observe "Active" filter is displayed
    And I cannotsee "clear-completed" filter is displayed

  Scenario: Verify Active filter shows all the todo list tasks when nothing is set to completed
    Given I observe the entries of todolist in "All" filter
    When User clicks on "Active" filter
    Then I observe the entries of todolist in "Active" filter
    And I verify the task list in "All" filter and "Active" filter are equal
    When User clicks on cancel symbol for "open restaurant at 9" in the list
    Then I cannotsee "open restaurant at 9" task in "Completed" list
    And I cannotsee "open restaurant at 9" task in "Active" list
    And I cannotsee "open restaurant at 9" task in "All" list


  Scenario: Verify the completed filter functionality when tasks are set to completed
    Given I observe the entries of todolist in "All" filter
    And I count the displayed list items in "All" filter
    When User clicks on task on todo list
      | open restaurant at 9 |
      | raise a bug            |
    And User clicks on "Completed" filter
    And I observe the entries of todolist in "Completed" filter
    When User clicks on cancel symbol for "open restaurant at 9" in the list
    Then I cannotsee "open restaurant at 9" task in "Completed" list
    And I cannotsee "open restaurant at 9" task in "Active" list
    And I cannotsee "open restaurant at 9" task in "All" list
    And I cannotsee "clear-completed" filter is displayed


  Scenario: Validate the clear completed filter functionality
    Given I observe the entries of todolist in "All" filter
    And I count the displayed list items in "All" filter
    When User clicks on task on todo list
      | open restaurant at 9 |
    And User clicks on "Completed" filter
    Then I observe "open restaurant at 9" task in "Completed" list
    And User clicks on "clear-completed" filter
    And I cannotsee "open restaurant at 9" task in "Active" list
    And I cannotsee "open restaurant at 9" task in "All" list
    And I cannotsee "open restaurant at 9" task in "Completed" list
    And I cannotsee "clear-completed" filter is displayed


  Scenario: Validate the All filter functionality
    Given I observe the entries of todolist in "All" filter
    And I count the displayed list items in "All" filter
    When User clicks on task on todo list
      | open restaurant at 9 |
    Then I observe the listSummary count is reduced by number of completed tasks in "All" filter
    And User clicks on "Completed" filter
    Then I observe "open restaurant at 9" task in "Completed" list
    And User clicks on "All" filter
    And I observe "open restaurant at 9" task in "All" list

  Scenario Outline: Validate list summary count for All and Active filter tasks

    Given I observe the entries of todolist in "<filter>" filter
    And I count the displayed list items in "<filter>" filter
    And I verify the list summary count in "All" filter is equal to list summary count in "Active" filter
    When User clicks on cancel symbol for "open restaurant at 9" in the list
    Then I verify the list summary count is updated in "<filter>" filter
    And I enter a todo task
      | attend meeting |
    And I verify the list summary count is updated in "<filter>" filter
    Examples:
      | filter |
      | All    |
      | Active |


  Scenario: Validate task completed toggle functionality

    Given I observe the entries of todolist in "All" filter
    And I count the displayed list items in "All" filter
    When User clicks on mark all as completed drop mark
    Then I observe the task list is marked to completed
    And I observe the entries of todolist in "Completed" filter
    And I cannotsee the entries of todolist in "Active" filter
    When User clicks on mark all as completed drop mark
    Then I observe the entries of todolist in "Active" filter
    And I cannotsee the entries of todolist in "Completed" filter


    Scenario: Validate edit task functionality
      Given I observe the entries of todolist in "All" filter
      When I edit the task "check inventory" to "check inventory"
      Then I should be able to see "check inventory" task in the filter












