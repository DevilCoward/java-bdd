package com.luffy.testautomation.appautomation.screens.android.wandoujia;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import com.luffy.testautomation.appautomation.screens.common.wandoujia.HomeScreen;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class HomeScreenAndroid extends HomeScreen
        implements Provider<HomeScreenAndroid> {

    @Inject
    public HomeScreenAndroid(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        uniquePageLocator = By.id("qv");
        avatarIcon = By.id("nt");
    }

    @Override
    public HomeScreenAndroid get() {
        return this;
    }
}
