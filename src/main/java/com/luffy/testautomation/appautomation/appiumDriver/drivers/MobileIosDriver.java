package com.luffy.testautomation.appautomation.appiumDriver.drivers;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.config.MyIosModule;
import com.luffy.testautomation.appautomation.config.WDAServer;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

/**
 * Used to create an Appium iOSDriver
 */
public class MobileIosDriver {
    private final Config config;

    private static final String wdaServerScript = "scripts/start-wdaserver.bash";

    @Inject
    public MobileIosDriver(Config config) {
        this.config = config;
    }

    /**
     * Used to create Appium Desired Capabilities for iOS
     */
    private DesiredCapabilities getCapabilities() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            //capabilities.setCapability("noReset", false);
            capabilities.setCapability("noReset", true);
            capabilities.setCapability("fullReset", false);
            capabilities.setCapability("automationName", "XCUITest");
            capabilities.setCapability("platformName", "iOS");
            //capabilities.setCapability("deviceName", MyIosModule.deviceModel());
            //capabilities.setCapability("udid", MyIosModule.deviceUdId());
            //capabilities.setCapability("platformVersion", "12.1");
            capabilities.setCapability("deviceName", "iPhone 11 Pro Max");
            capabilities.setCapability("platformVersion", "13.3");
            capabilities.setCapability(
                    "app", "/Users/luffy.y.wan_sp/Downloads/apk/MY/InstallmentsSampleApp.app");
            capabilities.setCapability("newCommandTimeout", 999999);
            if (config.hasPath("bundle")) {
                String iosPath = config.getString("bundle");
                capabilities.setCapability("app", iosPath);
            //} else {
            //    String absoluteIOSPath = config.getString("ios.app.path");
            //    capabilities.setCapability(
            //            "app", String.format(absoluteIOSPath, System.getProperty("user.dir")));
            }
            if (MyIosModule.isRealDevice) {
                capabilities.setCapability("webDriverAgentUrl", WDAServer.SERVER_URL);
                MyIosModule.execCommand(wdaServerScript + " " + WDAServer.getInstance().deviceId);
            }
            capabilities.setCapability(
                    "bundleId",
                    MyIosModule.getBundleId(capabilities.getCapability("app").toString()));

            MyIosModule.bundleId = capabilities.getCapability("bundleId").toString();
            MyIosModule.deviceName = capabilities.getCapability("deviceName").toString();
            MyIosModule.platformVersion = capabilities.getCapability("platformVersion").toString();

            return capabilities;
        } catch (Exception error) {
            throw new RuntimeException("Failed to create IOS Capabilities.", error);
        }
    }

    /**
     * Used to build a new IOSDriver instance
     */
    public AppiumDriver<MobileElement> buildIOSDriver() {
        try {
            return new IOSDriver<>(
                    new URL(
                            String.format(
                                    "http://localhost:%d/wd/hub",
                                    config.getInt("ios.server.port"))),
                    getCapabilities());

        } catch (Exception error) {
            throw new RuntimeException("Failed to build iOS Driver.", error);
        }
    }
}
