package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.luffy.testautomation.prjhelpers.LocaleCSVParser;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;

public class CreateInstalmentScreenIos_malaysia extends CreateInstalmentScreen {

    private final String applyInstalmentId =
            LocaleCSVParser.getLocaleValue("create_instalment_text");

    @Inject
    public CreateInstalmentScreenIos_malaysia(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, Config config) {
        super(appiumDriver, appiumHelpers, config);
        this.offerBannerTextId = "cardView";
    }

    @Override
    public CreateInstalmentScreen get() {
        return this;
    }

    @Override
    public void clickOnApplyInstalmentPlanLink() {
        appiumHelpers.waitForElementByAccessibilityId(applyInstalmentId).click();
    }
}
