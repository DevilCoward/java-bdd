package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * Data Structure for a single Test Run. Contains all pertinent information for the run to be
 * reported to Cedar
 */
public class RunData {
    private static final Logger log = LoggerFactory.getLogger(RunData.class);

    private static final String PASS = "PASS";
    private static final String PASSED = "Passed";
    private static final String FAIL = "FAIL";
    private static final String FAILED = "Failed";
    private static final String NOT_RUN = "Not Run";

    public String uuid;
    public String startDate;
    public String endDate;
    public List<FeatureData> featureDataList;
    public String status;

    private String country;
    private String l10n;

    private AppData appData;

    private String platform;
    private String platformVersion;
    private String deviceUDID;
    private String deviceModel;
    private String deviceManufacturer;
    private String deviceScreenSize;
    private String deviceName;

    public String hostMachine;
    public String environment;
    public String name;
    private String summary = "";

    public boolean isBrowserStackRun = false;
    public String browserStackSessionUrl;
    public String browserStackVideoUrl;
    public String browserStackDeviceLogsUrl;
    public String browserStackAppiumLogsUrl;
    public String browserStackLogsUrl;

    public RunData() {
        featureDataList = new ArrayList<>();
        appData = new AppData();
    }

    /**
     * Return all FeatureData for the specified URI
     *
     * @param uri URI of feature to be returned
     * @return FeatureData composed of information so far for the feature
     */
    public FeatureData getFeature(String uri) {
        FeatureData foundFeature = null;

        for (FeatureData featureData : featureDataList) {
            if (featureData.uri.equalsIgnoreCase(uri)) {
                foundFeature = featureData;
                break;
            }
        }
        return foundFeature;
    }

    /**
     * Given a run designation, return the TestData object associated with the Run Designation
     *
     * @param runDesignation The designation to be searched
     * @return TestData with details so far for the Test specified
     */
    public TestData getTest(String runDesignation) {
        TestData testData = null;

        for (FeatureData featureData : featureDataList) {
            for (TestData featureTest : featureData.testDataList) {
                if (featureTest.runDesignation.equalsIgnoreCase(runDesignation)) {
                    testData = featureTest;
                    break;
                }
            }
        }
        return testData;
    }

    /**
     * Update a given test in this run. Uses Run Designation to determine the corresponding test in
     * this run
     *
     * @param testData TestData object to be used to update the run
     */
    public void updateTest(TestData testData) {
        for (FeatureData featureData : featureDataList) {
            for (TestData featureTest : featureData.testDataList) {
                if (featureTest.runDesignation.equalsIgnoreCase(testData.runDesignation)) {
                    featureData.testDataList.set(
                            featureData.testDataList.indexOf(featureTest), testData);
                    break;
                }
            }
        }
    }

    /**
     * Get the overall status of the test run "Passed", "Failed", "Not Run", based on the pass/fail
     * count
     *
     * @return Status name
     */
    public String getOverallStatus() {
        int failCount, passCount, notRunCount, otherCount;
        failCount = passCount = notRunCount = otherCount = 0;

        for (FeatureData featureData : featureDataList) {
            for (TestData testData : featureData.testDataList) {
                String status = testData.status;
                if (status.contains(FAIL)) {
                    failCount++;
                } else if (status.contains(PASS)) {
                    passCount++;
                } else if (status.contains(NOT_RUN)) {
                    notRunCount++;
                } else {
                    otherCount++;
                }
            }
        }

        String status = PASSED;
        if (failCount > 0) {
            status = FAILED;
        } else if (failCount == 0 && passCount == 0) {
            status = NOT_RUN;
        }

        summary =
                String.format(
                        "%s\nRan: %d\nPassed:%d\nFailed:%d",
                        summary, passCount + failCount, passCount, failCount);

        return status;
    }

    /**
     * Append to the current summary a new line containing the text passed in. Prepends and appends
     * new line, so text can be rendered as a new paragraph essentially.
     *
     * @param text The text to append to the summary
     */
    public void addSummary(String text) {
        summary = summary.concat("\n" + text + "\n");
    }

    /**
     * Set the country name used in this run. Capitalizes value passed in.
     *
     * @param countryName Country name to set
     */
    public void setCountry(String countryName) {
        this.country = StringUtils.capitalize(countryName);
    }

    /**
     * Return the country used for this run in capitalized word case. e.g. Armenia
     *
     * @return Country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the localization (l10n) used for this run. Takes the 2 letter l10n name and sets it as
     * the full name of the l10n
     *
     * @param l10n the l10n to be set
     */
    public void setL10n(String l10n) {
        HashMap<String, String> l10nMap = new HashMap<>();
        l10nMap.put("ar", "Arabic");
        l10nMap.put("en", "English");
        l10nMap.put("es", "Spanish");
        l10nMap.put("el", "Greek");
        l10nMap.put("fr", "French");
        l10nMap.put("sc", "Simplified Chinese");
        l10nMap.put("tc", "Traditional Chinese");
        l10nMap.put("vn", "Vietnamese");

        String language = l10nMap.get(l10n);
        this.l10n = language == null ? l10n : language;
    }

    /**
     * Return the Localization being used for this run, e.g. Arabic, English, ...
     *
     * @return Name of localization
     */
    public String getL10n() {
        return l10n;
    }

    /**
     * Set the platform for this run. casts to proper name case
     *
     * @param platform the platform to set for this run
     */
    public void setPlatform(String platform) {
        switch (platform.toLowerCase()) {
            case "android":
                this.platform = "Android";
                break;
            case "ios":
                this.platform = "iOS";
                break;
            default:
                this.platform = platform;
        }
    }

    /**
     * Return the platform this run is associated with. e.g. Android, iOS
     *
     * @return String of platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Set the app data for this run
     *
     * @param appData AppData object for this run
     */
    public void setApp(AppData appData) {
        this.appData = appData;
    }

    /**
     * Get the app data associated with this run
     *
     * @return AppData object with info pertinent to the run
     */
    public AppData getApp() {
        return appData;
    }

    /**
     * Given the AppiumDriver for the session, set the app and device details used during the run
     *
     * @param appiumDriver AppiumDriver instance used during the run
     */
    public void setDetailsFromDriver(AppiumDriver appiumDriver) {
        JSONObject capabilities = new JSONObject(appiumDriver.getSessionDetails());

        AppData app = new AppData();

        log.info("Pausing for driver setup");
        try {
            // Race condition exists with current appium-browserstack session setup.
            // No query appears to exist to safely get the status at this time.
            // Hard coded sleep is in place until deterministic check can be found.
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {
        }
        log.info("Pause complete");

        if (isBrowserStackRun) {
            log.info("Getting BrowserStack Data");
            JSONObject automationSession =
                    getBrowserStackSession(appiumDriver.getSessionId().toString());

            try {
                browserStackAppiumLogsUrl = automationSession.get("appium_logs_url").toString();
                browserStackDeviceLogsUrl = automationSession.get("device_logs_url").toString();
                browserStackLogsUrl = automationSession.get("logs").toString();
                browserStackSessionUrl = automationSession.get("public_url").toString();
                browserStackVideoUrl = automationSession.get("video_url").toString();
            } catch (JSONException ignored) {
            }

            String cedar_uuid = System.getenv("cedar_run_uuid");
            if (cedar_uuid != null && !cedar_uuid.isEmpty()) {
                log.info("Run UUID Specified By Env Var: " + cedar_uuid);
                app.setAppFromBrowserStack(automationSession);
            }
        } else {
            app.setAppFromFile((String) capabilities.get("app"));
        }
        appData = app;

        String platformName = (String) capabilities.get("platformName");

        if (platformName.equalsIgnoreCase("android")) {
            Object deviceId = capabilities.get("deviceUDID");
            deviceUDID = deviceId == null ? "" : (String) deviceId;
            deviceModel = (String) capabilities.get("deviceModel");
            deviceManufacturer = (String) capabilities.get("deviceManufacturer");
            deviceScreenSize = (String) capabilities.get("deviceScreenSize");
            deviceName = (String) capabilities.get("deviceName");
        } else {
            Object deviceId = capabilities.get("udid");
            deviceName = (String) capabilities.get("deviceName");
            deviceUDID = deviceId == null ? "" : (String) deviceId;
            deviceModel = deviceName;
            deviceManufacturer = "Apple";
            deviceScreenSize = "";
        }
        platformVersion = (String) capabilities.get("platformVersion");
    }

    /**
     * Convert this RunData object to a JSON data structure
     *
     * @return JSONObject representing the data present in this run
     */
    public JSONObject toJson() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("name", name);
        jsonData.put("summary", summary);
        jsonData.put("status", status);
        jsonData.put("run_uuid", uuid);

        jsonData.put("start_date", startDate);
        jsonData.put("end_date", endDate);

        jsonData.put("environment", environment);
        jsonData.put("l10n", getL10n());
        jsonData.put("country", getCountry());
        jsonData.put("folder", getCountry());
        jsonData.put("iut_sw", getApp().toJson());
        jsonData.put("run_on", hostMachine);

        jsonData.put("platform", platform);
        jsonData.put("device_uuid", deviceUDID);
        jsonData.put("device_model", deviceModel);
        jsonData.put("device_manufacturer", deviceManufacturer);
        jsonData.put("platform_version", platformVersion);
        jsonData.put("screen_size", deviceScreenSize);
        jsonData.put("device_name", deviceName);

        JSONArray logFiles = new JSONArray();
        if (browserStackLogsUrl != null) {
            JSONObject logReference = new JSONObject();
            logReference.put("name", "Session Logs");
            logReference.put("uri", browserStackLogsUrl);
            logFiles.put(logReference);
        }

        if (browserStackAppiumLogsUrl != null) {
            JSONObject appiumLogReference = new JSONObject();
            appiumLogReference.put("name", "Appium Logs");
            appiumLogReference.put("uri", browserStackAppiumLogsUrl);
            logFiles.put(appiumLogReference);
        }

        if (browserStackDeviceLogsUrl != null) {
            JSONObject deviceLogReference = new JSONObject();
            deviceLogReference.put("name", "Device Logs");
            deviceLogReference.put("uri", browserStackDeviceLogsUrl);
            logFiles.put(deviceLogReference);
        }

        if (browserStackVideoUrl != null) {
            JSONObject sessionVideo = new JSONObject();
            sessionVideo.put("name", "Session Video");
            sessionVideo.put("uri", browserStackVideoUrl);
            logFiles.put(sessionVideo);
        }

        jsonData.put("logs", logFiles.toString());

        JSONArray externalLinks = new JSONArray();
        if (browserStackSessionUrl != null) {
            JSONObject browserStackSession = new JSONObject();
            browserStackSession.put("name", "BrowserStack Session");
            browserStackSession.put("uri", browserStackSessionUrl);
            externalLinks.put(browserStackSession);
        }
        jsonData.put("external_links", externalLinks.toString());

        return jsonData;
    }

    /**
     * Utilize the CI Helpers script to poll BrowserStack for session details
     *
     * @param sessionId The hashed session ID for the session to query
     * @return JSONObject containing data for Automation Session as presented by the BS API
     */
    private JSONObject getBrowserStackSession(String sessionId) {
        JSONObject automationSession = null;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath =
                Paths.get(
                        currentPath.toString(),
                        "ci",
                        "helpers",
                        "browserstack",
                        "browser_stack_api.py");
        Path pythonPath = Paths.get(currentPath.toString(), "ci", "venv", "bin", "python");

        Process process;
        try {
            process =
                    Runtime.getRuntime()
                            .exec(
                                    String.format(
                                            "%s %s --get_session %s",
                                            pythonPath, filePath.toString(), sessionId));

            process.waitFor(60, TimeUnit.SECONDS);

            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8));

            BufferedReader stdErr =
                    new BufferedReader(new InputStreamReader(process.getErrorStream(), UTF_8));

            String scriptOutput;
            while ((scriptOutput = stdErr.readLine()) != null) {
                log.warn("Script stdErr: " + scriptOutput);
            }

            StringBuilder json_string = new StringBuilder();
            while ((scriptOutput = stdInput.readLine()) != null) {
                log.info("Script stdOut: " + scriptOutput);
                json_string.append(scriptOutput);
            }

            JSONObject browserStackData = new JSONObject(json_string.toString().trim());

            JSONObject sessionDetails =
                    new JSONObject(browserStackData.get("session_details").toString());
            automationSession = new JSONObject(sessionDetails.get("automation_session").toString());

        } catch (Exception exception) {
            log.error(getStackTrace(exception));
        }

        return automationSession;
    }
}
