package com.luffy.testautomation.appautomation.appiumDriver;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.appiumDriver.drivers.MobileAndroidDriver;
import com.luffy.testautomation.appautomation.appiumDriver.drivers.MobileIosDriver;
import com.luffy.testautomation.appautomation.utilities.plugins.Cedar.CedarReporting;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;

/**
 * This Provider class is used to determine which type of Appium Driver to create as the session
 * driver.
 *
 * <p>Appium Driver types: BrowserStackDriver, MobileIOSDriver, and MobileAndroidDriver
 */
public class AppiumDriverProvider implements Provider<AppiumDriver<MobileElement>> {

    // Used to keep track of the current session Appium Driver
    // This should remain the same for Local AppiumDriver session
    // For Browserstack, session driver reset once every 90 mins (refer to
    // resetBrowserStackSession() hook)
    private static AppiumDriver<MobileElement> sessionDriver;
    private final boolean useAndroid = System.getProperty("platform").equalsIgnoreCase("android");

    private final Logger log = LoggerFactory.getLogger(AppiumDriverProvider.class);
    private final Config config;

    @Inject
    public AppiumDriverProvider(Config config) {
        this.config = config;
    }

    /**
     * Used to clear session driver
     */
    public static void clearSessionDriver() {
        sessionDriver = null;
    }

    @Override
    public AppiumDriver<MobileElement> get() {
        if (sessionDriver == null) {
            log.info("Building new Appium Driver");
            if (useAndroid) {
                // Android
                sessionDriver = new MobileAndroidDriver(config).buildAndroidDriver();
            } else {
                // IOS
                sessionDriver = new MobileIosDriver(config).buildIOSDriver();
            }
            CedarReporting.setAppiumDriver(sessionDriver);
            return sessionDriver;
        }
        return sessionDriver;
    }
}
