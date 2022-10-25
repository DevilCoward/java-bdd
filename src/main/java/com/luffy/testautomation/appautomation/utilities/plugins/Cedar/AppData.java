package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * AppData class is to provide a consistent data structure for information on the App(s) used during
 * test. Includes helper methods for determining details from an app based on its source.
 */
public class AppData {
    private static final Logger log = LoggerFactory.getLogger(AppData.class);

    private final String KEY_NAME = "name";
    private final String KEY_ENVIRONMENT = "environment";
    private final String KEY_VERSION = "version";
    private final String KEY_BUILD = "build";
    private final String KEY_COUNTRY = "country";
    private final String KEY_FILENAME = "file_name";
    private final String KEY_LOCATION = "artifact_location";
    private final String KEY_DESCRIPTION = "description";
    private final String KEY_BRANCH = "branch";
    private final String NOT_SPECIFIED = "Not Specified";

    private String name = NOT_SPECIFIED;
    private String description = NOT_SPECIFIED;
    private String country = NOT_SPECIFIED;
    private String environment = NOT_SPECIFIED;
    private String version = NOT_SPECIFIED;
    private String build = NOT_SPECIFIED;
    private String branch = NOT_SPECIFIED;
    private String filename = NOT_SPECIFIED;
    private String location = NOT_SPECIFIED;

    /**
     * Given a BrowserStack session ID, poll the session API and retrieve app data used during the
     * session. Set data based on info available from session
     *
     * @param automationSession The session data for the app to be queried
     */
    public void setAppFromBrowserStack(JSONObject automationSession) {

        try {
            log.info("Setting App from BrowserStack");

            JSONObject appDetails = new JSONObject(automationSession.get("app_details").toString());

            String fullAppName = (String) appDetails.get("app_custom_id");

            String customIdRegex = "(?<appName>[\\w-_]*)v(?<version>[\\w.]*)b(?<build>[\\d]*)";
            Pattern regexPattern = Pattern.compile(customIdRegex);
            Matcher matcher = regexPattern.matcher(fullAppName);

            if (matcher.find()) {
                String appCenterAppName = matcher.group("appName");
                name = appCenterAppName;
                version = matcher.group(KEY_VERSION);
                build = matcher.group(KEY_BUILD);

                String appCenterRegex =
                        "HSBC-(?<country>[\\w]*)-(?<environment>[\\w]*)-(?<branch>[\\w]*)";
                Pattern appCenterNamePattern = Pattern.compile(appCenterRegex);
                matcher = appCenterNamePattern.matcher(appCenterAppName);
                if (matcher.find()) {
                    environment = matcher.group(KEY_ENVIRONMENT);
                    branch = matcher.group(KEY_BRANCH);
                    country = matcher.group(KEY_COUNTRY);
                }
                description = appCenterAppName;
            } else {
                version = (String) appDetails.get("app_version");
                name = (String) appDetails.get("app_name");
                description = fullAppName;
            }
            filename = (String) appDetails.get("app_url");
            location = "BrowserStack";
        } catch (JSONException e) {
            log.warn("Error Getting BrowserStack Details:\n" + getStackTrace(e));
        }
    }

    /**
     * Given the filename of an app, parse as much information as possible from it and set the
     * intance values
     *
     * @param appDetail Filename of app to glean information from
     */
    public void setAppFromFile(String appDetail) {
        filename = appDetail.substring(appDetail.lastIndexOf("/") + 1);

        String androidFileRegex =
                "(?<country>[\\w]*)-(?<environment>[\\w]*)-(?<version>[\\w.]*)-([\\w-]*)-(?<build>[\\d]*)";
        Pattern regexPatter = Pattern.compile(androidFileRegex);
        Matcher matcher = regexPatter.matcher(filename);

        if (matcher.find()) {
            country = matcher.group(KEY_COUNTRY);
            version = matcher.group(KEY_VERSION);
            build = matcher.group(KEY_BUILD);
            environment = matcher.group(KEY_ENVIRONMENT);
            name = String.format("%s v%s b%s", country, version, build);
            description = filename;
        } else {
            name = filename;
        }
        location = "local";
    }

    /**
     * Convert this AppData object to a JSONObject data structure. JSON structure is to be
     * consistent with data expected from the Cedar API.
     *
     * @return JSONObject representing the data for this app
     */
    public JSONObject toJson() {
        JSONObject jsonData = new JSONObject();
        jsonData.put(KEY_NAME, name);
        jsonData.put(KEY_ENVIRONMENT, environment);
        jsonData.put(KEY_VERSION, version);
        jsonData.put(KEY_BUILD, build);
        jsonData.put(KEY_COUNTRY, country);
        jsonData.put(KEY_FILENAME, filename);
        jsonData.put(KEY_LOCATION, location);
        jsonData.put(KEY_DESCRIPTION, description);
        jsonData.put(KEY_BRANCH, branch);
        return jsonData;
    }
}
