package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.contracts.instalments.CreateInstalment;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;

public class CreateInstalmentScreen extends Base
        implements CreateInstalment, Provider<CreateInstalmentScreen> {

    protected AppiumDriver appiumDriver;
    protected AppiumHelpers appiumHelpers;
    protected Config config;

    protected final String createInstalmentButtonId = "createInstallmentButton";
    private final String activePlanHeaderId = "activePlanTextView";
    private final String applyInstalmentPlanLinkId = "productShelfListItemTitle";

    @Inject
    public CreateInstalmentScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, Config config) {
        super(appiumDriver);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.config = config;
    }

    @Override
    public CreateInstalmentScreen get() {
        if (System.getProperty("country").equalsIgnoreCase("malaysia")) {
            return new CreateInstalmentScreenAndroid_malaysia(
                    appiumDriver, appiumHelpers, config);
        }
        return this;
    }

    @Override
    public void clickOnApplyInstalmentPlanLink() {
        appiumHelpers.waitForElementById(applyInstalmentPlanLinkId).click();
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementById(activePlanHeaderId, timeout);
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElementById(activePlanHeaderId);
    }
}
