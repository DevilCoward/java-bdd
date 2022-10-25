package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;

public class CreateInstalmentScreenAndroid_malaysia extends CreateInstalmentScreen {

    @Inject
    public CreateInstalmentScreenAndroid_malaysia(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, Config config) {
        super(appiumDriver, appiumHelpers, config);
    }

    @Override
    public CreateInstalmentScreen get() {
        return this;
    }

    @Override
    public void clickOnApplyInstalmentPlanLink() {
        appiumHelpers.waitForElementById(createInstalmentButtonId).click();
    }
}
