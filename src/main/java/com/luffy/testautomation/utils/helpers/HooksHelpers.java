package com.luffy.testautomation.utils.helpers;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;

/**
 * Helper class to be used by Hooks classes.
 *
 * @return An instantiated {@link HooksHelpers} class
 */
public class HooksHelpers {
    private final AppiumDriver appiumDriver;
    private final Config config;

    /**
     * Creates a new instance
     *
     * @param appiumDriver is an instance of {@link AppiumDriver} or class that extends it.
     * @param config is an instance of Config
     */
    @Inject
    public HooksHelpers(AppiumDriver appiumDriver, Config config) {
        this.appiumDriver = appiumDriver;
        this.config = config;
    }

    public void restartApp() {
        String platform = System.getProperty("platform");
        if (platform.equalsIgnoreCase("ios")) {
            restartIosApp();
        } else if (platform.equalsIgnoreCase("android")) {
            restartAndroidApp();
        }
    }

    /**
     * Restarts the android application under test. appPackage, appActivity and appWaitActivity
     * capabilities in DesiredCapabilities are required to be set
     *
     * @since Appium Version 1.9.2
     */
    public void restartAndroidApp() {
        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        Object appwaitActivity = appiumDriver.getCapabilities().getCapability("appWaitActivity");
        String appWaitActivityName =
                appwaitActivity == null
                        ? config.getString("appWaitActivity")
                        : appwaitActivity.toString();

        Activity appActivity =
                new Activity(
                        appiumDriver.getCapabilities().getCapability("appPackage").toString(),
                        appiumDriver.getCapabilities().getCapability("appActivity").toString());
        appActivity.setAppWaitActivity(appWaitActivityName);
        androidDriver.startActivity(appActivity);
    }

    /**
     * Restarts the iOS application under test. bundleId in DesiredCapabilities is required to be
     * set
     *
     * @since Appium Version 1.9.2
     */
    public void restartIosApp() {
        String bundleId = appiumDriver.getCapabilities().getCapability("bundleId").toString();
        int appState =
                Integer.parseInt(
                        appiumDriver
                                .executeScript(
                                        "mobile: queryAppState",
                                        ImmutableMap.of("bundleId", bundleId))
                                .toString());
        if (appState == 4) {
            appiumDriver.executeScript(
                    "mobile: terminateApp", ImmutableMap.of("bundleId", bundleId));
        }
        appiumDriver.executeScript("mobile: launchApp", ImmutableMap.of("bundleId", bundleId));
    }
}
