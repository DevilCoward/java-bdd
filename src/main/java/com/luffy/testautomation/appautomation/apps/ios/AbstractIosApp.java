package com.luffy.testautomation.appautomation.apps.ios;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.apps.App;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;

import java.util.HashMap;

public abstract class AbstractIosApp implements App {

    @Inject private AppiumDriver appiumDriver;
    @Inject private Config config;
    private String name;
    private String bundleId;

    public AbstractIosApp(String name, String bundleId) {
        this.name = name;
        this.bundleId = bundleId;
    }

    private String getPath() {
        String key = String.join(".", "ios", name, "path");
        String path = config.getString(key);
        return String.format(path, System.getProperty("user.dir"));
    }

    @Override
    public void install() {
        HashMap params = new HashMap();
        params.put("app", getPath());

        appiumDriver.executeScript("mobile: installApp", params);
    }

    @Override
    public void launch() {
        HashMap params = new HashMap();
        params.put("bundleId", bundleId);

        appiumDriver.executeScript("mobile: launchApp", params);
    }
}
