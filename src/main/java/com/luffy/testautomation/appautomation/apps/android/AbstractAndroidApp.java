package com.luffy.testautomation.appautomation.apps.android;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.apps.App;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.appmanagement.AndroidInstallApplicationOptions;

public abstract class AbstractAndroidApp implements App {

    @Inject private AppiumDriver appiumDriver;
    @Inject private Config config;
    private String name;
    private Activity activity;

    public AbstractAndroidApp(String name, String packageId, String activity, String waitActivity) {
        this.name = name;
        this.activity = new Activity(packageId, activity).setAppWaitPackage(waitActivity);
    }

    private String getPath() {
        String key = String.join(".", "android", name, "path");
        String path = config.getString(key);
        return String.format(path, System.getProperty("user.dir"));
    }

    @Override
    public void install() {
        AndroidInstallApplicationOptions options = new AndroidInstallApplicationOptions();
        options.withAllowTestPackagesEnabled();
        appiumDriver.installApp(getPath(), options);
    }

    @Override
    public void launch() {
        ((AndroidDriver) appiumDriver).startActivity(activity);
    }
}
