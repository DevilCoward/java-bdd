package com.luffy.testautomation.steps;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.contracts.instalments.*;
import io.cucumber.java8.En;
import org.hamcrest.core.IsEqual;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class InstalmentSampleAppSteps implements En {

    @Inject
    public InstalmentSampleAppSteps(
            InstalmentSampleAppLogin instalmentSampleAppLogin,
            BalanceInstalment balanceInstalment,
            InstalmentOnboarding instalmentOnboarding,
            InstalmentMultipleOnboarding instalmentMultipleOnboarding,
            CreateInstalment createInstalment) {

        Given("^the user selects entity from Entity dropdown$",
                instalmentSampleAppLogin::selectEntity);

        When("^the user selects (_*.*) from Active Plans options on test harness page$",
                instalmentSampleAppLogin::selectActivePlans);

        When("^the user clicks on continue button to proceed on Sample app$",
                instalmentSampleAppLogin::clickContinueButton);

        When("^the user clicks on Launch Spend Instalment button on Sample app$",
                instalmentSampleAppLogin::clickLaunchInstalmentButtonOnSampleApp);

        Then("^the user is navigated to Onboarding screen$",
                () -> {
                    boolean expected = true;
                    boolean actual = instalmentOnboarding.isVisible(20);
                    String reasonForFailure = "Onboarding screen did not appear as expected ";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                });

        When("^the user clicks bottom button on onboarding screen$",
                instalmentOnboarding::clickGetStartedButton);


        Then("^the user is navigated to multiple onboarding screen$",
                () -> {
                    boolean expectedResult = true;
                    String reasonForFailure = "the multiple onboarding screen is not displayed";
                    boolean actualResult = instalmentMultipleOnboarding.isVisible();
                    assertThat(reasonForFailure, actualResult, IsEqual.equalTo(expectedResult));
                });

        When("^the user clicks the bottom button on multiple onboarding screen$",
                instalmentMultipleOnboarding::clickContinueButton);

        Then("^the user is navigated to balance instalment page$",
                () -> {
                    boolean expectedResult = true;
                    String reasonForFailure = "the balance instalment screen is not displayed";
                    boolean actualResult = balanceInstalment.isVisible();
                    assertThat(reasonForFailure, actualResult, IsEqual.equalTo(expectedResult));
                });

        When("^the user clicks the close button on offer cap banner$",
                balanceInstalment::clickCloseOnOfferCapBanner);

        Then("^the offer cap banner is disappeared$",
                () -> {
                    boolean expectedResult = false;
                    String reasonForFailure = "the offer cap banner is not disappeared";
                    boolean actualResult = balanceInstalment.isOfferCapBannerDisplayed();
                    assertThat(reasonForFailure, actualResult, IsEqual.equalTo(expectedResult));
                });

        Then("^the user is navigated to instalment dashboard screen$",
                () -> {
                    boolean expectedResult = true;
                    String reasonForFailure = "the instalment dashboard screen is not displayed";
                    boolean actualResult = createInstalment.isVisible();
                    assertThat(reasonForFailure, actualResult, IsEqual.equalTo(expectedResult));
                });

        When("^the user clicks apply for another instalment plan link$",
                createInstalment::clickOnApplyInstalmentPlanLink);

        When("^the user inputs (_*.*) in the amount input field$",
                balanceInstalment::inputInstalmentAmount);

        Then("^the user should see the inline error message is displayed$",
                () -> {
                    boolean expectedResult = true;
                    String reasonForFailure = "the inline error message is display incorrectly";
                    boolean actualResult = balanceInstalment.isInlineMessageDisplayed();
                    assertThat(reasonForFailure, actualResult, IsEqual.equalTo(expectedResult));
                });
    }
}
