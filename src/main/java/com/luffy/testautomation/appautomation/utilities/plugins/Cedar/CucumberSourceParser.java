package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CucumberSourceParser class is used to convert plain text cucumber feature files into useable data
 * structures
 */
public class CucumberSourceParser {

    private final String FEATURE = "Feature";
    private final String SCENARIO = "Scenario:";
    private final String SCENARIO_OUTLINE = "Scenario Outline:";
    private final String TAG_INDICATOR = "@";
    private final String EXAMPLES = "Examples:";
    private final String EXAMPLE_DELIMITER = "|";

    private String sourceURI;
    private List<String> sourceLines;
    private int currentLine = 0;

    /**
     * Given the URI of the source file and the content of the source file, parse it into
     * FeatureData. Returns the FeatureData object containing TestData and StepData parsed from it.
     * Further processing of StepData is required after the fact to glean specifics of step data.
     * Iterates the file from top to bottom and calls helper methods to parse individual
     * scenario/scenario outlines where found.
     *
     * @param uri Unique Resource Identifier of the file being parsed
     * @param source The content of the source file as a String
     * @return FeatureData object containing as much information as can be gleaned from the source
     */
    public FeatureData parseFeatureSource(String uri, String source) {
        sourceURI = uri;
        FeatureData featureData = new FeatureData(uri);

        sourceLines = new ArrayList<>(Arrays.asList(source.split("\n")));
        boolean featureFound = false;

        while (!sourceLines.isEmpty()) {
            String line = readSourceLine();
            String tags;

            // Check for Scenario Tags
            if (line.startsWith(TAG_INDICATOR)) {
                tags = line.replace("@", "");
                line = readSourceLine();
            } else {
                tags = "";
            }

            if (line.contains(FEATURE) && !featureFound) {
                // Feature Header found
                featureData.name = line.split(":", -1)[1].trim();
                featureData.tags = tags;
                featureFound = true;
            } else if (line.contains(SCENARIO)) {
                // Scenario Header found
                pushSourceLine(line);
                tags = (featureData.tags + " " + tags).trim();
                featureData.testDataList.add(parseScenario(tags));
            } else if (line.contains(SCENARIO_OUTLINE)) {
                pushSourceLine(line);
                tags = (featureData.tags + " " + tags).trim();
                featureData.testDataList.addAll(parseScenarioOutline(tags));
            }
        }
        return featureData;
    }

    /**
     * Parse a scenario outline from the current file location. Iterates lines until the next
     * Scenario is found, or EOF
     *
     * @param tags Tags associated with this test, gleaned from the Feature
     * @return List of TestData objects for each Scenario example in the outline
     */
    private List<TestData> parseScenarioOutline(String tags) {
        String pipeSplitRegex = "(\\|)(\\s)*(?<value>[^\\|]*)";
        Pattern pipeSplitPattern = Pattern.compile(pipeSplitRegex);

        List<TestData> testsList = new ArrayList<>();

        String line = readSourceLine();
        List<StepData> steps = new ArrayList<>();
        String scenarioName = line.split(":", -1)[1].trim();

        try {
            line = ""; // blank line for further parsing
            while (!line.contains(SCENARIO_OUTLINE)
                    && !line.contains(SCENARIO)
                    && !line.startsWith(TAG_INDICATOR)
                    && !line.contains(EXAMPLES)) {
                // Read steps until the next scenario
                if (line.length() > 0) {
                    steps.add(new StepData(line));
                }
                line = readSourceLine();
            }

            // When examples line or tags line is hit, parse each scenario outline example
            // read until end of examples
            // if tags line hit, reset tags to base tags, append new tags
            String scenarioTags = tags;
            List<String> keyList = new ArrayList<>();

            while (line.contains(EXAMPLES)
                    || line.contains(EXAMPLE_DELIMITER)
                    || line.contains(TAG_INDICATOR)) {

                if (line.contains(TAG_INDICATOR)) {
                    // reading tag line for examples set, or cases have not been separated by space
                    scenarioTags = tags; // reset example tags to the scenario/feature tags
                    String exampleTags = line.replace("@", "");
                    scenarioTags = scenarioTags + " " + exampleTags;
                    scenarioTags = scenarioTags.trim();

                    // peek at next line
                    line = readSourceLine();
                    if (line.contains(SCENARIO) || line.contains(SCENARIO_OUTLINE)) {
                        // file is poorly formatted but valid. Push tags back and move on to next
                        // scenario
                        pushSourceLine(line);
                        break;
                    }
                    continue; // continue parsing examples
                } else if (line.contains(EXAMPLES)) {
                    // read the following line, as it contains the example keys
                    line = readSourceLine();
                    keyList = new ArrayList<>(); // reset the keys list
                    Matcher keysMatcher = pipeSplitPattern.matcher(line);

                    // populate example keys
                    while (keysMatcher.find()) {
                        String keyName = keysMatcher.group("value").trim();
                        if (keyName.length() > 0) {
                            keyList.add(keyName);
                        }
                    }
                } else if (line.contains(EXAMPLE_DELIMITER)) {
                    // hitting an individual example. Parse the key-values and create scenarios
                    TestData testExample = new TestData();
                    testExample.isSubTest = true;
                    testExample.name = scenarioName;
                    testExample.tags = scenarioTags;
                    testExample.stepDataList = steps;

                    testExample.uri =
                            String.format("%s;%s;%s", sourceURI, scenarioName, line).trim();
                    testExample.runDesignation =
                            String.format(
                                            "%s:%s # %s",
                                            sourceURI.split(":", -1)[1], currentLine, scenarioName)
                                    .trim();

                    // Parse example parameters
                    Matcher paramsMatch = pipeSplitPattern.matcher(line);
                    List<String> paramsList = new ArrayList<>();
                    while (paramsMatch.find()) {
                        String param = paramsMatch.group("value");
                        if (param.length() > 0) {
                            paramsList.add(param);
                        }
                    }
                    testExample.parameters =
                            (HashMap<String, String>)
                                    IntStream.range(0, keyList.size())
                                            .boxed()
                                            .collect(
                                                    Collectors.toMap(
                                                            keyList::get, paramsList::get));
                    testsList.add(testExample);
                }
                line = readSourceLine();
            }

            pushSourceLine(line);

        } catch (IndexOutOfBoundsException ignored) {
        }

        return testsList;
    }

    /**
     * Parse a Scenario (not Scenario Outline) from the current line in the file. Iterates until the
     * next Scenario is found or EOF.
     *
     * @param tags Tags associated with this scenario, gleaned from the feature
     * @return TestData object for the Scenario
     */
    private TestData parseScenario(String tags) {
        TestData currentTestData = new TestData();
        currentTestData.tags = tags;
        String line = readSourceLine();

        String scenarioName = line.split(":", -1)[1].trim();
        currentTestData.name = scenarioName;
        currentTestData.uri = String.format("%s;%s", sourceURI, scenarioName).trim();
        currentTestData.runDesignation =
                String.format("%s:%s # %s", sourceURI.split(":", -1)[1], currentLine, scenarioName)
                        .trim();
        try {
            line = ""; // blank line for further parsing
            while (!line.contains(SCENARIO_OUTLINE)
                    && !line.contains(SCENARIO)
                    && !line.startsWith(TAG_INDICATOR)
                    && !line.contains(EXAMPLES)) {
                // Read steps until the next scenario
                if (line.length() > 0) {
                    currentTestData.stepDataList.add(new StepData(line));
                }
                line = readSourceLine();
            }
            // Push the last line back onto the stack for future parsing
            pushSourceLine(line);

        } catch (IndexOutOfBoundsException ignored) {
        }

        return currentTestData;
    }

    /**
     * Helper method to keep track of line read in file Iterates line read with each call and
     * returns the white-space trimmed line
     *
     * @return String containing data for the line read during the call
     */
    private String readSourceLine() {
        currentLine++;
        return sourceLines.remove(0).trim();
    }

    /**
     * Helper method for back tracking in the file. Inserts the specified line back onto the parsing
     * stack (index 0) and decrements the current line count.
     *
     * @param line String data for the line to be inserted back onto the read queue
     */
    private void pushSourceLine(String line) {
        currentLine--;
        sourceLines.add(0, line);
    }
}
