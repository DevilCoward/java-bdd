package com.luffy.testautomation.appautomation.config;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import com.luffy.testautomation.utils.helpers.Defaults;
import com.luffy.testautomation.utils.helpers.Exceptional;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class MyIosGridModule extends AbstractIosModule {

    private final Logger log = LoggerFactory.getLogger(MyIosGridModule.class);

    private static final String bundleIdScript = "scripts/extract-bundleId-from-ipa.bash";
    private static final String wdaServerScript = "scripts/start-customwdaserver.bash";
    private static final String cleanUpScript = "scripts/iproxy-xcodebuild-cleanup.bash";

    public static int threadPoolCounter = 0;
    private String deviceUnderTest = "";
    private boolean isRealDevice = false;

    @Provides
    @Singleton
    public Config configProvider() throws Exception {
        Config defaultConfig = ConfigFactory.parseResources("application.conf");

        // Pick config based on Country -> Env
        // Separate configuration files for environments (if needed)
        String countryConfigPath =
                String.format(
                        "config/%s/%s_env.conf",
                        System.getProperty("country"), System.getProperty("env"));
        Config countryConfig = ConfigFactory.parseResources(countryConfigPath);
        Config deviceConfig = ConfigFactory.parseResources("devices.conf");
        Config config =
                ConfigFactory.systemEnvironment()
                        .withFallback(ConfigFactory.systemProperties())
                        .withFallback(countryConfig)
                        .withFallback(deviceConfig)
                        .withFallback(defaultConfig)
                        .resolve();

        String platform = System.getProperty("platform") + ".grid";
        if (threadPoolCounter == 0) MyIosModule.execCommand(cleanUpScript);
        deviceUnderTest =
                ConfigFactory.parseResources("devices.conf")
                        .getString(platform)
                        .split(",", -1)[threadPoolCounter++];
        // deviceUnderTest = Thread.currentThread().getName();
        System.out.println("Device Thread Under Test : " + deviceUnderTest);
        String dataSource = config.getConfig(deviceUnderTest).getString("dataSource");
        log.info("RESOLVED environment  as: " + dataSource);
        return config.getConfig(dataSource).withFallback(config);
    }

    @Singleton
    @Provides
    public AppiumDriver provideAppiumDriver(Config config)
            throws Exception {
        String platformVersion = config.getConfig(deviceUnderTest).getString("platformVersion");
        String deviceName = config.getConfig(deviceUnderTest).getString("deviceName");
        String browserStackOption = System.getProperty("browserstack");
        if (Boolean.parseBoolean(browserStackOption)) {
            return null;
        } else {
            String deviceUdId = config.getConfig(deviceUnderTest).getString("deviceUdId");
            int appiumPort = config.getConfig(deviceUnderTest).getInt("appiumPort");
            int wdaLocalPort = config.getConfig(deviceUnderTest).getInt("wdaLocalPort");
            isRealDevice = !deviceUdId.equals("");
            log.info(
                    "Current Device Details => "
                            + deviceUdId
                            + " | "
                            + deviceName
                            + " | "
                            + isRealDevice
                            + " | "
                            + platformVersion
                            + " | "
                            + wdaLocalPort
                            + " | "
                            + appiumPort);
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("noReset", false);
                capabilities.setCapability("fullReset", false);
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("platformVersion", platformVersion);
                capabilities.setCapability("deviceName", deviceName);
                capabilities.setCapability("udid", deviceUdId);
                capabilities.setCapability("newCommandTimeout", 999999);
                if (config.hasPath("bundle")) {
                    String iosPath = config.getString("bundle");
                    capabilities.setCapability("app", iosPath);
                } else {
                    String absoluteIOSPath = config.getString("ios.app.path");
                    capabilities.setCapability(
                            "app", String.format(absoluteIOSPath, System.getProperty("user.dir")));
                }
                capabilities.setCapability(
                        "bundleId", bundleId(capabilities.getCapability("app").toString()));
                URL url = new URL(String.format("http://localhost:%d/wd/hub", appiumPort));
                log.info("*** Connecting to Appium at " + url);

                if (isRealDevice) {
                    com.luffy.testautomation.appautomation.config.CustomWDAServer customWDAServer = new com.luffy.testautomation.appautomation.config.CustomWDAServer(deviceUdId, wdaLocalPort);
                    capabilities.setCapability(
                            "webDriverAgentUrl", customWDAServer.getSERVER_URL());
                    MyIosModule.execCommand(
                            wdaServerScript
                                    + " "
                                    + customWDAServer.getDeviceId()
                                    + " "
                                    + customWDAServer.retrieveProvisioningProfile().get(1)
                                    + " "
                                    + customWDAServer.retrieveProvisioningProfile().get(2)
                                    + " "
                                    + customWDAServer.retrieveProvisioningProfile().get(0)
                                    + " "
                                    + wdaLocalPort);
                } else capabilities.setCapability("wdaLocalPort", wdaLocalPort);
                return new io.appium.java_client.ios.IOSDriver<>(url, capabilities);

            } catch (RuntimeException | IOException ex) {
                log.error("*** ERROR STARTING APPIUM *** " + Exceptional.describeCauseOf(ex));
                throw ex;
            }
        }
    }

    @Provides
    public WebDriverWait provideWebdriverWait(AppiumDriver appiumDriver) {
        return new WebDriverWait(appiumDriver, Defaults.WAIT_TIME.getTime());
    }

    private String bundleId(String appPath) throws IOException {
        String bundleId = "";
        String simBundleId = "uk.co.hsbc.enterprise.hsbcmobilebanking.dev";
        if (isRealDevice) {
            bundleId = new CommandLineExecutor().execute(bundleIdScript + " " + appPath);
        } else {
            bundleId = simBundleId;
        }
        return bundleId;
    }
}
