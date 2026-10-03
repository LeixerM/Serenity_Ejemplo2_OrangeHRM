@employees
Feature: Manage employees in OrangeHRM PIM
  As an HR administrator
  I want to add, find and remove employees
  So that the employee directory stays accurate

  Every scenario uses a unique employee and removes it afterwards (see the @After hook).

  Background:
    Given Hana is logged in to OrangeHRM as an administrator

  @create
  Scenario: Add an employee with a profile photo
    When she adds a new employee with a profile photo
    Then the employee profile shows the full name and the uploaded photo

  @search
  Scenario: Find an employee by name
    Given a new employee exists
    When she searches the employee list by the employee's full name
    Then the employee is the only result, with the right id and names

  @delete
  Scenario: Delete an employee
    Given a new employee exists
    When she deletes the employee from the employee list
    Then she is told "Successfully Deleted"
    And searching by the employee id finds no records
