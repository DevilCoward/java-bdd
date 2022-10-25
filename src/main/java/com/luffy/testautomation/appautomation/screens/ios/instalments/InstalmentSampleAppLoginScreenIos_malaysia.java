package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;

public class InstalmentSampleAppLoginScreenIos_malaysia extends InstalmentSampleAppLoginScreen {

    @Inject
    public InstalmentSampleAppLoginScreenIos_malaysia(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
    }

    @Override
    public InstalmentSampleAppLoginScreen get() {
        return this;
    }

    @Override
    public void selectActivePlans(String activePlansType) {
        // ios and android fields are different in the sample apps
        if (activePlansType.contains("Plans Empty")) {
            appiumHelpers.waitForElement(selectPlanClassChain).sendKeys(selectPlanEmptyValue);
        } else if (activePlansType.contains("Plans Invalid")) {
            appiumHelpers.waitForElement(selectPlanClassChain).sendKeys(selectPlanInvalidValue);
        } else {
            appiumHelpers.waitForElement(selectPlanClassChain).sendKeys(selectDefaultValue);
        }
    }
}
