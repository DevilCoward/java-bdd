@wandoujia
Feature: wandoujia app Scenario
  I want to open wandoujia app
  In order to check function

  Scenario: the user check wandoujia function demo
    Given the user is on home page of wandoujia app
    When the user clicks the avatar icon
    Then the user is navigated to my pea pod screen
    And the user should see login text
    When the user clicks back button
    Then the user is navigated to home screen
    And the user restarts app
