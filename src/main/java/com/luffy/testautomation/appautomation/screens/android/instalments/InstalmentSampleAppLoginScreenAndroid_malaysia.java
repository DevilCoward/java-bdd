package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;

public class InstalmentSampleAppLoginScreenAndroid_malaysia extends InstalmentSampleAppLoginScreen {

    @Inject
    public InstalmentSampleAppLoginScreenAndroid_malaysia(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
    }

    @Override
    public InstalmentSampleAppLoginScreen get() {
        return this;
    }

    @Override
    public void selectActivePlans(String planStatus) {
        appiumHelpers.waitForElementById(historicalPlanListTestHarnessPageId).click();
        appiumHelpers.waitForElementById(accountListItemId);
        accountList.stream()
                .filter(webElement -> webElement.getText().contains(planStatus))
                .findFirst()
                .get()
                .click();
    }
}
