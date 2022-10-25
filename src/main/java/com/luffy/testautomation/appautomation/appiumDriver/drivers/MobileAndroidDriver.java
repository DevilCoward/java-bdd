package com.luffy.testautomation.appautomation.appiumDriver.drivers;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.config.MyAndroidModule;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

/** Used to create an Appium AndroidDriver */
public class MobileAndroidDriver {

    private final Config config;

    @Inject
    public MobileAndroidDriver(Config config) {
        this.config = config;
    }

    /** Used to create Appium Desired Capabilities for Android */
    private DesiredCapabilities getCapabilities() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            //capabilities.setCapability("noReset", false);
            capabilities.setCapability("noReset", true);
            capabilities.setCapability("fullReset", false);
            capabilities.setCapability("platformName", "Android");
            //capabilities.setCapability("platformVersion", MyAndroidModule.osVersion());
            //capabilities.setCapability("deviceName", MyAndroidModule.deviceName());
            capabilities.setCapability("platformVersion", "9");
            capabilities.setCapability("deviceName", "Pixel 2 XL");
            capabilities.setCapability("newCommandTimeout", "1000");
            capabilities.setCapability("automationName", "UiAutomator2");
            //capabilities.setCapability("appWaitActivity", MyAndroidModule.appWaitActivity());
            //capabilities.setCapability("appActivity", MyAndroidModule.appActivity());
            //capabilities.setCapability("appPackage", MyAndroidModule.appPackageCap());
            //wandoujia
            //capabilities.setCapability("appWaitActivity", "com.pp.assistant.*");
            //capabilities.setCapability("appActivity", "com.pp.assistant.activity.PPMainActivity");
            //capabilities.setCapability("appPackage", "com.wandoujia.phoenix2");
            //sample app
            capabilities.setCapability("appWaitActivity", "com.hsbc.mobile.installmentshost.*");
            capabilities.setCapability("appActivity", "com.hsbc.mobile.installmentshost.entityconfig.ConfigActivity");
            capabilities.setCapability("appPackage", "com.hsbc.mobile.installmentshost");
            capabilities.setCapability("autoGrantPermissions", "false");
            capabilities.setCapability("--session-override", "true");
            //capabilities.setCapability(
            //        "app",
            //        String.format(
            //                config.getString("android.app.path"), System.getProperty("user.dir")));
            return capabilities;
        } catch (Exception error) {
            throw new RuntimeException("Failed to generate Android capabilities", error);
        }
    }

    /** Used to build a new AndroidDriver instance */
    public AppiumDriver<MobileElement> buildAndroidDriver() {
        try {
            return new AndroidDriver<>(
                    new URL(
                            String.format(
                                    "http://localhost:%d/wd/hub",
                                    config.getInt("android.server.port"))),
                    getCapabilities());

        } catch (Exception error) {
            throw new RuntimeException("Failed to build Android Driver.", error);
        }
    }
}
