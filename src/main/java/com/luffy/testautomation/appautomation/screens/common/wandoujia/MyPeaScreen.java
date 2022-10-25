package com.luffy.testautomation.appautomation.screens.common.wandoujia;

import com.luffy.testautomation.appautomation.contracts.wandoujia.MyPea;
import com.luffy.testautomation.appautomation.screens.BaseScreen;
import com.luffy.testautomation.prjhelpers.ButtonHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public abstract class MyPeaScreen extends BaseScreen
        implements MyPea, ButtonHelpers {

    protected ActionHelpers actionHelpers;

    protected By loginText;
    protected By backButton;

    public MyPeaScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public String getLoginText() {
        return appiumHelpers.waitForElement(loginText).getText();
    }

    @Override
    public void clickBackButton() {
        appiumHelpers.waitForElement(backButton).click();
    }
}
