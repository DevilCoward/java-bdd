package com.luffy.testautomation.appautomation.screens.ios.wandoujia;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import com.luffy.testautomation.appautomation.screens.common.wandoujia.HomeScreen;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class HomeScreenIos extends HomeScreen
        implements Provider<HomeScreenIos> {

    @Inject
    public HomeScreenIos(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        uniquePageLocator = By.id("");
        avatarIcon = By.id("");
    }

    @Override
    public HomeScreenIos get() {
        return this;
    }
}
