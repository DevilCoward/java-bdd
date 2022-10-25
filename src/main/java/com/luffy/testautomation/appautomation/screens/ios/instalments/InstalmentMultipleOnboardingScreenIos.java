package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.Instalment.InstalmentMultipleOnboardingScreen;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class InstalmentMultipleOnboardingScreenIos extends InstalmentMultipleOnboardingScreen
        implements Provider<InstalmentMultipleOnboardingScreenIos> {

    @Inject
    public InstalmentMultipleOnboardingScreenIos(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        productImage = By.name("headerImageView");
        skipButton = By.id("skipButton");
        continueButton = MobileBy.AccessibilityId("createInstallmentButton");
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElement(continueButton, timeout)
                && !appiumHelpers.checkForElement(skipButton, timeout);
    }

    @Override
    public InstalmentMultipleOnboardingScreenIos get() {
        return this;
    }
}
