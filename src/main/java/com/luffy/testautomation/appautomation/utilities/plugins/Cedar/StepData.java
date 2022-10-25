package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** Data structure for a Single Step used in a Test Scenario */
public class StepData {
    public static final String STATUS_KEY = "status";
    public static final String DURATION_KEY = "duration";
    public static final String END_DATE_KEY = "end_date";
    public static final String MESSAGE_KEY = "message";
    public static final String RAW_KEY = "raw";
    public static final String PATTERN_KEY = "pattern";
    public static final String LOCATION_KEY = "location";

    public String name;
    public String parameters;
    public String pattern;
    public String location;
    public String status;
    public String errorMessage;
    public String duration;
    public String endDate;

    private static HashSet<String> patternList = new HashSet<>();
    private static List<StepData> stepsList = new ArrayList<>();

    public StepData(String name) {
        this.name = name.replaceFirst("(And|Given|When|Then) ", "").trim();
        this.parameters = "";
        StepData.stepsList.add(this);
    }

    public JSONObject toJSON() {
        JSONObject stepData = new JSONObject();
        stepData.put(RAW_KEY, pattern);
        stepData.put(PATTERN_KEY, name);
        stepData.put(LOCATION_KEY, location);
        stepData.put(STATUS_KEY, status);
        stepData.put(DURATION_KEY, duration);
        stepData.put(END_DATE_KEY, endDate);
        stepData.put(MESSAGE_KEY, errorMessage);
        return stepData;
    }

    /**
     * Due to load order of Cucumber plugins, patterns are not known until after all steps have been
     * loaded. To associate each step in a feature file with its corresponding step regex, this must
     * be called after sources are read. When a pattern is added, list of steps is searched and any
     * that match the regex are associated to the regex
     *
     * @param location The automation path to the file. Given limitations, the path may solely be
     *     the filename
     * @param pattern The step regex pattern to be added
     */
    public static void addStepPattern(String location, String pattern) {
        patternList.add(pattern);
        for (StepData step : stepsList) {
            if (step.name.matches(pattern)) {
                step.pattern = pattern;
                step.location = location;
            }
        }
    }
}
