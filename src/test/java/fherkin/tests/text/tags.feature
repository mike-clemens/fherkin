#
# Feature file containing tags
#
@featureTag1 @featureTag2
@featureTag3
Feature: Tagged feature

  @scenarioTag1 @scenarioTag2
  @scenarioTag3
  Scenario: Tagged scenario
    Given I have 5 cucumbers
     When I eat 3 cucumbers
     Then I will have 2 cucumbers

  @scenarioOutlineTag1 @scenarioOutlineTag2
  @scenarioOutlineTag3
  Scenario Outline: Tagged scenario outline
    Given I have <start> cucumbers
     When I eat <eat> cucumbers
     Then I will have <remaining> cucumbers
    Examples:
      | start | eat | remaining |
      |     5 |   3 |         2 |
      |     3 |   2 |         1 |
      |     2 |   1 |         1 |
