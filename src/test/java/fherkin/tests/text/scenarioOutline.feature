#
# Feature file containing a simple scenario outline
#
Feature: Scenario outline feature file

  Background: Buy some cucumbers
    * Go to the store
    * Buy some cucumbers

  Scenario Outline: Simple scenario outline
    Given I have <start> cucumbers
     When I eat <eat> cucumbers
     Then I will have <remaining> cucumbers
    Examples:
      | start | eat | remaining |
      |     5 |   3 |         2 |
      |     3 |   2 |         1 |
      |     2 |   1 |         1 |
