package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.luffy.testautomation.appautomation.utilities.plugins.Cedar.StepData.*;

/** Data Structure for a Single Test Scenario (case) */
public class TestData {
    public static final String environmentError = "Environment";

    private static final ImmutableMap<String, String> failureLookup =
            ImmutableMap.<String, String>builder()
                    .put("java.lang.AssertionError", "Assertion")
                    .put("WebDriverException", "Element Not Found")
                    .put("com.typesafe.config.ConfigException", "Config Exception")
                    .put("selenium.TimeoutException", "Element Not Found")
                    .put("Unknown", "Script Error")
                    .put(environmentError, environmentError)
                    .build();

    public String name;
    public HashMap<String, String> parameters;
    public String startTime;
    public String endTime;
    public String duration;
    public String uri;
    public String tags;
    public String runDesignation;
    public String status;
    public String errorMessage;
    public String reasonForFailure;
    public boolean isSubTest = false;

    public List<StepData> stepDataList;

    public TestData() {
        parameters = new HashMap<>();
        name = "Not Set";
        duration = null;
        uri = "Not Set";
        tags = "";
        runDesignation = "";
        status = "Not Run";
        errorMessage = "";
        reasonForFailure = "";
        stepDataList = new ArrayList<>();
    }

    /** Update the specified step with details on its execution */
    public void updateStep(String stepLocation, JSONObject stepDetails) {
        for (StepData stepData : stepDataList) {
            if (stepData.location != null && stepData.location.equals(stepLocation)) {
                stepData.status = (String) stepDetails.get(STATUS_KEY);
                stepData.errorMessage = (String) stepDetails.get(MESSAGE_KEY);
                stepData.duration = (String) stepDetails.get(DURATION_KEY);
                stepData.endDate = (String) stepDetails.get(END_DATE_KEY);
            }
        }
    }

    /**
     * Given an error message, parse the Reason For Failure from it.
     *
     * @param errorMessage The error message to be parsed
     * @return String name for the Reason For Failure
     */
    public static String parseRFF(String errorMessage) {
        if (errorMessage == null) {
            return "";
        }
        for (Object key : failureLookup.keySet()) {
            if (errorMessage.contains((String) key)) {
                return failureLookup.get(key);
            }
        }
        return failureLookup.get("Unknown");
    }

    /**
     * Parse the Test data structure into a JSON Object in format expected by the Cedar API
     *
     * @return
     */
    public JSONObject toJson() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("test_name", name);
        jsonData.put("automation_path", runDesignation);
        jsonData.put("sub_test", isSubTest);

        Pattern pattern = Pattern.compile("(src/test/tests/)(?<feature>[^.]*)(.feature)");
        Matcher matcher = pattern.matcher(runDesignation);
        String featureLocation = "";
        if (matcher.find()) {
            featureLocation = matcher.group("feature");
        }
        jsonData.put("folder", featureLocation);

        JSONArray steps = new JSONArray();
        for (StepData stepData : stepDataList) {
            steps.put(stepData.toJSON());
        }
        jsonData.put("steps", steps.toString());
        jsonData.put("tags", tags);
        jsonData.put("test_type", "E2E");
        jsonData.put("parameters", parameters);

        List<String> platforms = new ArrayList<>();
        if (!tags.contains("ios")) {
            // only add Android platform if test has not been specifically tagged as ios
            platforms.add("Android");
        }
        if (!tags.contains("android")) {
            // only add iOS platform if test has not been specifically tagged as Android
            platforms.add("iOS");
        }
        jsonData.put("platforms", platforms);

        List<String> environments = new ArrayList<>();
        environments.add("Cert");
        jsonData.put("environment", environments);

        String summary = String.format("%s\n%s", uri, errorMessage);
        jsonData.put("summary", summary);
        jsonData.put("status", status);
        jsonData.put("rff", reasonForFailure);

        return jsonData;
    }
}
