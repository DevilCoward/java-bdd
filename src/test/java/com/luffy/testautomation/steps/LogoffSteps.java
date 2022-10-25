package com.luffy.testautomation.steps;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.HooksHelpers;
import io.cucumber.java8.En;

public class LogoffSteps implements En {

    @Inject
    public LogoffSteps(HooksHelpers hooksHelpers) {

        Given("^the user restarts app$", hooksHelpers::restartApp);
    }
}
