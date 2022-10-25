@sampleApp_my
Feature: Instalment Scenario
  I want to login on Instalment sample app
  In order to view and create instalment plan

  Scenario: The user can navigate to balance instalment page and close the offer cap banner
    Given the user selects entity from Entity dropdown
    When the user selects Plans Empty from Active Plans options on test harness page
    And the user clicks on continue button to proceed on Sample app
    And the user clicks on Launch Spend Instalment button on Sample app
    Then the user is navigated to Onboarding screen
    When the user clicks bottom button on onboarding screen
    Then the user is navigated to multiple onboarding screen
    When the user clicks the bottom button on multiple onboarding screen
    Then the user is navigated to balance instalment page
    When the user clicks the close button on offer cap banner
    Then the offer cap banner is disappeared
    And the user restarts app

  Scenario: The user can see the inline error message when input amount exceed offer cap and lower than 500
    Given the user selects entity from Entity dropdown
    And the user clicks on continue button to proceed on Sample app
    And the user clicks on Launch Spend Instalment button on Sample app
    Then the user is navigated to instalment dashboard screen
    When the user clicks apply for another instalment plan link
    Then the user is navigated to balance instalment page
    When the user inputs 50001.00 in the amount input field
    Then the user should see the inline error message is displayed
    When the user inputs 499.00 in the amount input field
    Then the user should see the inline error message is displayed
    And the user restarts app
