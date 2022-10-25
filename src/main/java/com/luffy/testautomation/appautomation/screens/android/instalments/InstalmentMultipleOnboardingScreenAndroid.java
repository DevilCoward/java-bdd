package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.Instalment.InstalmentMultipleOnboardingScreen;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class InstalmentMultipleOnboardingScreenAndroid extends InstalmentMultipleOnboardingScreen
        implements Provider<InstalmentMultipleOnboardingScreenAndroid> {

    @Inject
    public InstalmentMultipleOnboardingScreenAndroid(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        productImage = By.id("featureHeaderImageView");
        skipButton = By.id("skipButton");
        continueButton = By.id(androidContinueButtonId);
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForVisibilityOfElementsBy(productImage, timeout)
                && !appiumHelpers.checkForElement(skipButton, timeout);
    }

    @Override
    public InstalmentMultipleOnboardingScreenAndroid get() {
        return this;
    }
}
