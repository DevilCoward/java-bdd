package com.luffy.testautomation.appautomation.screens.common.Instalment;

import com.luffy.testautomation.appautomation.contracts.instalments.InstalmentMultipleOnboarding;
import com.luffy.testautomation.appautomation.screens.BaseScreen;
import com.luffy.testautomation.prjhelpers.ButtonHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public abstract class InstalmentMultipleOnboardingScreen extends BaseScreen
        implements InstalmentMultipleOnboarding, ButtonHelpers {

    protected ActionHelpers actionHelpers;

    protected By productImage;
    protected By skipButton;
    protected By continueButton;

    public InstalmentMultipleOnboardingScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElement(productImage);
    }

    @Override
    public void clickContinueButton() {
        actionHelpers.swipeToElement(continueButton).click();
    }
}
