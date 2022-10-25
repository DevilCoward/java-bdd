package com.luffy.testautomation.utils.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Appium {
    public static AppiumDriver forDevice(String deviceId, String deviceName) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(
                    "appPackage", "uk.co.hsbc.enterprise.hsbcmobilebanking.cert");
            capabilities.setCapability(
                    "appActivity", "com.hsbc.mobilebanking.splash.SplashActivity");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("noReset", false);
            capabilities.setCapability("udid", deviceId);
            capabilities.setCapability("deviceName", deviceName);
            return new AndroidDriver(new URL("http://localhost:4725/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bug: appium url is invalid", e);
        }
    }
}
