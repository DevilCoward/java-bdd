package com.luffy.testautomation.appautomation.screens.common.wandoujia;

import com.luffy.testautomation.appautomation.contracts.wandoujia.Home;
import com.luffy.testautomation.appautomation.screens.BaseScreen;
import com.luffy.testautomation.prjhelpers.ButtonHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public abstract class HomeScreen extends BaseScreen
        implements Home, ButtonHelpers {

    protected ActionHelpers actionHelpers;

    protected By avatarIcon;

    public HomeScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public void clickAvatarIcon() {
        appiumHelpers.waitForElement(avatarIcon).click();
    }
}
