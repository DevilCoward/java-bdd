package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.contracts.instalments.InstalmentOnboarding;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AndroidHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;

import javax.inject.Inject;

import static com.luffy.testautomation.prjhelpers.ButtonHelpers.androidContinueButtonId;

public class InstalmentOnboardingScreen extends Base
        implements InstalmentOnboarding, Provider<InstalmentOnboardingScreen> {

    protected AppiumHelpers appiumHelpers;
    protected AppiumDriver appiumDriver;
    protected AndroidHelpers androidHelpers;
    protected ActionHelpers actionHelpers;

    protected String getStartedButtonId = androidContinueButtonId;

    @Inject
    public InstalmentOnboardingScreen(
            AppiumDriver appiumDriver,
            AppiumHelpers appiumHelpers,
            AndroidHelpers androidHelpers,
            ActionHelpers actionHelpers) {
        super(appiumDriver);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.androidHelpers = androidHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public void clickGetStartedButton() {
        waitVisible();
        appiumHelpers.waitForElementById(getStartedButtonId).click();
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementById(getStartedButtonId, timeout);
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElementById(getStartedButtonId);
    }

    @Override
    public InstalmentOnboardingScreen get() {
        if (System.getProperty("country").equalsIgnoreCase("malaysia")) {
            return new InstalmentOnboardingScreenAndroid_malaysia(
                    appiumDriver, appiumHelpers, androidHelpers, actionHelpers);
        }
        return this;
    }
}
