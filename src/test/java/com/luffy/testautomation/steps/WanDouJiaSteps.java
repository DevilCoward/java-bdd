package com.luffy.testautomation.steps;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.contracts.wandoujia.*;
import com.luffy.testautomation.prjhelpers.LocaleCSVParser;
import io.cucumber.java8.En;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WanDouJiaSteps implements En {

    @Inject
    public WanDouJiaSteps(
            Home home,
            MyPea myPea) {

        Given("^the user is on home page of wandoujia app$",
                () -> {
                    boolean expected = true;
                    boolean actual = home.isVisible(20);
                    String reasonForFailure =
                            "home screen did not appear as expected ";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                });


        When("the user clicks the avatar icon$", home::clickAvatarIcon);

        Then("^the user is navigated to my pea pod screen$",
                () -> {
                    boolean expected = true;
                    boolean actual = myPea.isVisible(20);
                    String reasonForFailure =
                            "Select transaction screen did not appear as expected ";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                });

        Then("^the user should see login text$",
                () -> {
                    String expected = LocaleCSVParser.getLocaleValue("login_text");
                    String actual = myPea.getLoginText();
                    String reasonForFailure = "payment schedule screen did not appear as expected";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                });

        When("the user clicks back button$", myPea::clickBackButton);

        Then("^the user is navigated to home screen$",
                () -> {
                    boolean expected = true;
                    boolean actual = home.isVisible(20);
                    String reasonForFailure =
                            "Select transaction screen did not appear as expected ";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                });
    }
}
