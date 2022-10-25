package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.contracts.instalments.InstalmentOnboarding;
import com.luffy.testautomation.prjhelpers.ButtonHelpers;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import io.appium.java_client.AppiumDriver;

public class InstalmentOnboardingScreen extends Base
        implements InstalmentOnboarding, ButtonHelpers {

    private AppiumHelpers appiumHelpers;

    private final String continueButtonId = "createInstallmentButton";

    @Inject
    public InstalmentOnboardingScreen(
            AppiumDriver appiumDriver,
            AppiumHelpers appiumHelpers) {
        super(appiumDriver);
        this.appiumHelpers = appiumHelpers;
    }

    @Override
    public void clickGetStartedButton() {
        appiumHelpers.waitForSeconds(3);
        appiumHelpers.waitForElementByAccessibilityId(continueButtonId).click();
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementByAccessibilityId(continueButtonId, timeout);
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElementByAccessibilityId(continueButtonId);
    }
}
