


#
# Feature file containing blank lines prior to the first comment
#
Feature: Scenario feature file

  Background: Buy some cucumbers
    * Go to the store
    * Buy some cucumbers

  Scenario: Simple scenario
    Given I have 5 cucumbers
      But I somehow acquire another 5 cucumbers
     When I eat 3 cucumbers
      And I throw away another 3 cucumbers
     Then I will have 4 cucumbers
