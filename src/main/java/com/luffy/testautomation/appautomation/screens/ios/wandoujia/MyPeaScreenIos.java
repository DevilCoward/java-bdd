package com.luffy.testautomation.appautomation.screens.ios.wandoujia;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.wandoujia.MyPeaScreen;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyPeaScreenIos extends MyPeaScreen
        implements Provider<MyPeaScreenIos> {

    @Inject
    public MyPeaScreenIos(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        uniquePageLocator = By.id("");
        loginText = By.id("");
        backButton = By.id("");
    }

    @Override
    public MyPeaScreenIos get() {
        return this;
    }
}
