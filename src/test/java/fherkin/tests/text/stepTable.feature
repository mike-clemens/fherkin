#
# Feature file containing scenarios with data tables under steps
# Note: This is not a complete gherkin recipe and will likely be terrible if you attempt
#
Feature: Step table feature file

  Scenario: Steps with data tables
    Given I have the following ingredients:
          | Item     | Count | Unit   |
          | Cucumber |     7 | Pieces |
          | Salt     |   0.5 | Cups   |
          | Sugar    |     8 | Cups   |
          | Vinegar  |     6 | Cups   |
     When I boil these ingredients together:
          | Item    |
          | Salt    |
          | Sugar   |
          | Vinegar |
      And I pour the result over the cucumbers
     Then I may have something resembling a gherkin
