package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for a single feature file, contains reference to each individual Test within the
 * feature
 */
public class FeatureData {

    public final String uri;
    public String name;
    public List<TestData> testDataList;
    public String tags;

    public FeatureData(String uri) {
        this.uri = uri;
        testDataList = new ArrayList<>();
    }
}
