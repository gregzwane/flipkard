@search @sort @price @Regression
Feature: Search for Galaxy Z Fold and sort by price

  Background:
    Given I navigate to the test URL "https://www.flipkart.com/"   # replace with actual test URL
    And I close any popup or login modal if present

  @search
  Scenario Outline: Search for "Galaxy Z Fold" and sort price Low to High
    When I search for "<product>"
    Then I should see results for "<product>"
    When I sort by price "Low to High"
    Then the products are displayed in ascending order of price

    Examples:
      | product        |
      | Galaxy Z Fold 5 |
      | Galaxy Z Fold 6 |
      | Galaxy Z Fold 7 |

  @price
  Scenario Outline: Search for "Galaxy Z Fold" and sort price High to Low
    When I search for "<product>"
    Then I should see results for "<product>"
    When I sort by price "High to Low"
    Then the products are displayed in descending order of price

    Examples:
      | product        |
      | Galaxy Z Fold 5 |
      | Galaxy Z Fold 6 |
      | Galaxy Z Fold 7 |