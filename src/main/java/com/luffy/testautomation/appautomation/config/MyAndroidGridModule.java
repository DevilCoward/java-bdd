package com.luffy.testautomation.appautomation.config;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import com.luffy.testautomation.utils.helpers.Defaults;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

public class MyAndroidGridModule extends AbstractAndroidModule {

    private final Logger log = LoggerFactory.getLogger(MyAndroidGridModule.class);

    private String deviceUnderTest = "";
    private static int threadPoolCounter = 0;
    private String appPackage = "in.hsbc.hsbcindia.cert";

    @Provides
    @Singleton
    public Config configProvider() {

        // Pick config based on Country -> Env
        // Separate configuration files for environments (if needed)
        String countryConfigPath =
                String.format(
                        "config/%s/%s_env.conf",
                        System.getProperty("country"), System.getProperty("env"));
        Config countryConfig = ConfigFactory.parseResources(countryConfigPath);
        Config defaultConfig = ConfigFactory.parseResources("application.conf");
        Config deviceConfig = ConfigFactory.parseResources("devices.conf");
        Config config =
                ConfigFactory.systemEnvironment()
                        .withFallback(ConfigFactory.systemProperties())
                        .withFallback(countryConfig)
                        .withFallback(defaultConfig)
                        .withFallback(deviceConfig)
                        .resolve();

        String platform = System.getProperty("platform") + ".grid";
        deviceUnderTest =
                ConfigFactory.parseResources("devices.conf")
                        .getString(platform)
                        .split(",", -1)[threadPoolCounter++];
        log.info("Device Thread Under Test : " + deviceUnderTest);
        String dataSource = config.getConfig(deviceUnderTest).getString("dataSource");
        log.info("RESOLVED environment as: " + dataSource);
        return config.getConfig(dataSource).withFallback(config);
    }

    @Provides
    @Singleton
    public AppiumDriver provideAppiumDriver(Config config)
            throws IOException {
        String deviceName = config.getConfig(deviceUnderTest).getString("deviceName");
        String osVersion = config.getConfig(deviceUnderTest).getString("osVersion");
        String browserStackOption = System.getProperty("browserstack");
        if (Boolean.parseBoolean(browserStackOption)) {
            return null;
        } else {
            Integer appiumPort = config.getConfig(deviceUnderTest).getInt("appiumPort");
            String deviceId = config.getConfig(deviceUnderTest).getString("deviceId");
            log.info(
                    "Current Device Details => "
                            + deviceId
                            + " | "
                            + deviceName
                            + " | "
                            + appPackage
                            + " | "
                            + appiumPort
                            + " | "
                            + osVersion);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("noReset", false);
            capabilities.setCapability("fullReset", false);
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", osVersion);
            capabilities.setCapability("udid", deviceId);
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("newCommandTimeout", "180");
            capabilities.setCapability("automationName", "UiAutomator2");
            capabilities.setCapability("appWaitActivity", "com.hsbc.mobilebanking.*");
            capabilities.setCapability(
                    "appActivity", "com.hsbc.mobilebanking.splash.SplashActivity");
            capabilities.setCapability("appPackage", appPackage);
            capabilities.setCapability("autoGrantPermissions", "false");
            capabilities.setCapability(
                    "app",
                    String.format(
                            config.getString("android.app.path"), System.getProperty("user.dir")));
            return new io.appium.java_client.android.AndroidDriver(
                    new URL(String.format("http://localhost:%d/wd/hub", appiumPort)), capabilities);
        }
    }

    @Provides
    public WebDriverWait provideWebdriverWait(AppiumDriver appiumDriver) {
        WebDriverWait wait = new WebDriverWait(appiumDriver, Defaults.WAIT_TIME.getTime());
        wait.ignoring(NoSuchElementException.class);
        return wait;
    }

    @SuppressWarnings("unused")
    private String osVersion(String deviceId) throws IOException {
        String osVersionCommand = "adb -s " + deviceId + " shell getprop ro.build.version.release";
        return new CommandLineExecutor().executePipedCommand(osVersionCommand);
    }

    @SuppressWarnings("unused")
    private String deviceName(String deviceId) throws IOException {
        String deviceNameCommand = "adb -s " + deviceId + " shell getprop ro.product.model";
        return new CommandLineExecutor().executePipedCommand(deviceNameCommand);
    }

    @SuppressWarnings("unused")
    private String appPackageCap(String deviceId) throws IOException {
        String appPackageCommand =
                "adb -s "
                        + deviceId
                        + " shell pm list packages | grep hsbc.hsbc | cut -d ':' -f2 | grep ";
        String country = System.getProperty("country").toLowerCase();
        return new CommandLineExecutor().executePipedCommand(appPackageCommand + country);
    }
}
