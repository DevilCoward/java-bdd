package com.luffy.testautomation.utils.common;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.PageFactory;

public class Base {

    public Base(AppiumDriver appiumDriver) {
        PageFactory.initElements(appiumDriver, this);
    }
}
