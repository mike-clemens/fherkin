#
# Feature file containing a feature description split across multiple lines
#
Feature: Long feature description
         The feature section can be multiple lines, and all lines prior to one
         of the gherkin keywords are considered to be part of the feature
         description.

  Scenario: Eat some cucumbers
    Given I have 5 cucumbers
     When I eat 3 cucumbers
     Then I will have 2 cucumbers
