package com.luffy.testautomation.appautomation.screens.android.wandoujia;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.wandoujia.MyPeaScreen;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyPeaScreenAndroid extends MyPeaScreen
        implements Provider<MyPeaScreenAndroid> {

    @Inject
    public MyPeaScreenAndroid(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        uniquePageLocator = By.id("ao4");
        loginText = By.id("bh");
        backButton = By.id("a_8");
    }

    @Override
    public MyPeaScreenAndroid get() {
        return this;
    }
}
