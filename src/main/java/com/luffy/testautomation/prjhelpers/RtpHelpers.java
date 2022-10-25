package com.luffy.testautomation.prjhelpers;

import io.appium.java_client.AppiumDriver;
import javax.inject.Inject;

public class RtpHelpers {
    private AppiumDriver appiumDriver;

    @Inject
    public RtpHelpers(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public void acceptPopUpPermission() {
        try {
            appiumDriver.switchTo().alert().accept();
        } catch (Exception e) {
        }
    }
}
