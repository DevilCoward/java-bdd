package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.AndroidHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;

public class InstalmentOnboardingScreenAndroid_malaysia extends InstalmentOnboardingScreen {

    @Inject
    public InstalmentOnboardingScreenAndroid_malaysia(
            AppiumDriver appiumDriver,
            AppiumHelpers appiumHelpers,
            AndroidHelpers androidHelpers,
            ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, androidHelpers, actionHelpers);
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementById(getStartedButtonId, timeout);
    }
}
