package com.luffy.testautomation.prjhelpers;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import io.appium.java_client.AppiumDriver;
import java.io.IOException;

public class AppHelpers {
    private AppiumDriver appiumDriver;

    @Inject
    public AppHelpers(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }

    public void reinstallApp() {
        if (System.getProperty("platform").equalsIgnoreCase("android")) {
            reinstallAppAndroid();
        } else if (System.getProperty("platform").equalsIgnoreCase("ios")) {
            reinstallAppIos();
        }
    }

    public void reinstallAppAndroid() {
        // Workaround to re-installing app as some apk builds fails to launch after install
        // close app, clear app data, launch apps
        appiumDriver.closeApp();
        try {
            resetAndroidApp();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset app data");
        }
        appiumDriver.launchApp();
    }

    public void reinstallAppIos() {
        appiumDriver.removeApp(appiumDriver.getCapabilities().getCapability("bundleId").toString());
        appiumDriver.installApp(appiumDriver.getCapabilities().getCapability("app").toString());
        appiumDriver.launchApp();
    }

    public void resetAndroidApp() throws IOException {
        String browserStackOption = System.getProperty("browserstack");
        if (Boolean.parseBoolean(browserStackOption)) {
            appiumDriver.resetApp();
        } else {
            String appPackage =
                    appiumDriver.getCapabilities().getCapability("appPackage").toString();
            String cmd = "adb shell pm clear " + appPackage;
            new CommandLineExecutor().executePipedCommand(cmd);
        }
    }
}
