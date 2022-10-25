package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import cucumber.api.SummaryPrinter;
import cucumber.api.event.*;
import cucumber.api.formatter.NiceAppendable;
import io.appium.java_client.AppiumDriver;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import static com.luffy.testautomation.appautomation.utilities.plugins.Cedar.StepData.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class CedarReporting implements ConcurrentEventListener, SummaryPrinter {

    private final NiceAppendable out;
    private RunData runData = new RunData();
    private TestData currentTest;

    private final CedarApi cedarApi;

    private final boolean bulkUpdate;
    private final boolean reportLive;

    private static AppiumDriver sessionAppiumDriver;
    private boolean detailsSet = false;

    private static String externalRFF = null;

    public static final String CEDAR_UUID_VAR = "CEDAR_UUID";
    private static final Path previousUUIDFILE =
            Paths.get(
                    System.getProperty("user.dir"), "build", "reports", "tests", "cedar_uuid.txt");

    /**
     * Plugin allows for hooking API calls to Cedar reporting system into test framework. Each
     * triggered event can be used to make an API call to the reporting system: - Source Read: Read
     * all Feature files - Step Defined: Parse all steps in Step Files - Test Run Started: Executed
     * before any tests - Test Case Start: Executed for each Test Scenario - Test Step Start:
     * Executed for each step run - Test Step End: Executed after each test step and before the next
     * step - Test Case End: Executed after each Test Case and before the next starts - Test Run
     * End: Executed after all tests have completed, successfully or not Plugin can be enabled by
     * adding the plugin to cukeWipeTest e.g. below: plugin = { "pretty",
     * "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
     * "json:build/reports/tests/cucumber-extent/cucumber_report.json",
     * "com.hsbc.digital.testautomation.global.mobilextestautomation.utilities.plugins.Cedar.CedarReporting:build/reports/tests/Cedar.txt"
     * },
     *
     * @param out Logging mechanism
     */
    public CedarReporting(Appendable out) {
        this.out = new NiceAppendable(out);
        this.cedarApi = new CedarApi();

        this.bulkUpdate = false;
        this.reportLive = true;
    }

    /**
     * Called to associate methods to be called for each event
     *
     * @param publisher EventPublisher that handles all events in the framework
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, this::sourceRead);

        publisher.registerHandlerFor(TestStepFinished.class, this::testStepFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::testStepStarted);

        publisher.registerHandlerFor(TestCaseStarted.class, this::testCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::testCaseFinished);

        publisher.registerHandlerFor(TestRunStarted.class, this::testRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::testRunFinished);
    }

    /**
     * First events executed. Called for each Feature file loaded
     *
     * @param event TestSourceRead event data
     */
    private void sourceRead(TestSourceRead event) {
        CucumberSourceParser cucumberSourceParser = new CucumberSourceParser();
        runData.featureDataList.add(
                cucumberSourceParser.parseFeatureSource(event.uri, event.source));
    }

    /**
     * Executed prior to each step
     *
     * @param event TestStepStarted event
     */
    private void testStepStarted(TestStepStarted event) {}

    /**
     * Executed after each step that was run
     *
     * @param event TestStepFinished event data
     */
    private void testStepFinished(TestStepFinished event) {
        try {
            if (currentTest != null) {
                JSONObject stepDetails = new JSONObject();
                stepDetails.put(STATUS_KEY, event.result.getStatus().firstLetterCapitalizedName());
                String errorMessage = event.result.getErrorMessage();
                errorMessage = errorMessage == null ? "" : errorMessage;
                stepDetails.put(MESSAGE_KEY, errorMessage);
                long durationMilliseconds = event.result.getDuration() / 100000;
                stepDetails.put(DURATION_KEY, Long.toString(durationMilliseconds));
                stepDetails.put(
                        END_DATE_KEY, Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
                currentTest.updateStep(event.testStep.getCodeLocation(), stepDetails);
            }
        } catch (Exception e) {
            logPrint("EXCEPTION On Step Finished: " + e.toString() + "\n" + getStackTrace(e));
        }
    }

    /**
     * Executed before each test case. Called for each scenario.
     *
     * @param event TestCaseStarted event data
     */
    private void testCaseStarted(TestCaseStarted event) {
        logPrint("Started Case: " + event.testCase.getScenarioDesignation());

        try {
            TestData testData = runData.getTest(event.testCase.getScenarioDesignation());
            testData.startTime = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
            currentTest = testData;

            if (!detailsSet) {
                logPrint("Setting run details");
                runData.setDetailsFromDriver(sessionAppiumDriver);
                detailsSet = true;
                cedarApi.updateRun(runData);
            }
        } catch (Exception e) {
            logPrint("Error on Test Start: " + e.toString() + "\n" + getStackTrace(e));
        }
    }

    /**
     * Executed after each test execution on pass, fail, or throw
     *
     * @param event TestCaseFinished event data
     */
    private void testCaseFinished(TestCaseFinished event) {
        logPrint("Ending: " + event.testCase.getScenarioDesignation());
        try {
            TestData testData = runData.getTest(event.testCase.getScenarioDesignation());
            testData.status = event.result.getStatus().toString();

            String errorMessage = event.result.getErrorMessage();
            if (externalRFF != null) {
                testData.reasonForFailure = externalRFF;
                externalRFF = null;
            } else {
                testData.reasonForFailure = TestData.parseRFF(errorMessage);
            }
            testData.errorMessage = errorMessage;

            testData.endTime = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
            testData.duration = event.result.getDuration().toString();

            runData.updateTest(testData);

            if (runData.uuid != null && reportLive) {
                logPrint("Updating Run for Result: " + testData.name);
                cedarApi.submitResult(runData, testData);
                logPrint("Updated Run");
            }
        } catch (Exception e) {
            logPrint(
                    "EXCEPTION On Test Finished("
                            + event.testCase.getScenarioDesignation()
                            + ")\n"
                            + getStackTrace(e));
        }
        currentTest = null;
    }

    /**
     * Called before execution of tests but after each source read and step defined
     *
     * @param event TestRunStarted event data
     */
    private void testRunStarted(TestRunStarted event) {
        try {
            if (reportLive) {
                runData.startDate = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
                runData.status = "In Progress";
                runData.setCountry(System.getProperty("country"));
                runData.setL10n(System.getProperty("locale"));
                runData.setPlatform(System.getProperty("platform"));
                try {
                    runData.hostMachine = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException e) {
                    runData.hostMachine = "Unknown";
                }

                runData.environment = "Cert";

                String runName = "";
                String browserStackOption = System.getProperty("browserstack");
                if (Boolean.parseBoolean(browserStackOption)) {
                    runData.isBrowserStackRun = true;
                    runName = "BrowserStack";

                    String app_id = System.getenv("BROWSERSTACK_APP_ID");
                    runData.addSummary("Using App:" + app_id);
                }
                runName =
                        String.format(
                                "%s %s %s", runName, runData.getCountry(), runData.getPlatform());
                runData.name = runName;

                // Check for UUID specified by file
                String cedarUuid;
                try {
                    Scanner fileScanner = new Scanner(previousUUIDFILE, UTF_8.name());
                    while (fileScanner.hasNext()) {
                        cedarUuid = fileScanner.next();
                    }
                    fileScanner.close();
                } catch (IOException ignored) {
                }

                // Check for Environment Variable specified test run
                cedarUuid = System.getenv("cedar_run_uuid");

                if (cedarUuid == null) {
                    cedarUuid = cedarApi.createNewRun(runData);
                    System.setProperty(CEDAR_UUID_VAR, cedarUuid);

                    try {
                        File outputFile = new File(previousUUIDFILE.toAbsolutePath().toString());
                        outputFile.getParentFile().mkdirs();
                        outputFile.createNewFile();

                        Writer fileWriter = Files.newBufferedWriter(outputFile.toPath(), UTF_8);
                        ;

                        fileWriter.write(cedarUuid + "\n");
                        fileWriter.flush();
                        fileWriter.close();

                    } catch (IOException exception) {
                        logPrint("IO EXCEPTION: " + exception);
                    }
                } else {
                    logPrint("Previous UUID Detected: " + cedarUuid);
                }
                runData.uuid = cedarUuid;
            }
            logPrint("Run Started: " + runData.uuid);
        } catch (Exception e) {
            logPrint("EXCEPTION On Run Start: " + e.toString() + "\n" + getStackTrace(e));
        }
    }

    /**
     * Called after execution of all tests
     *
     * @param event TestRunFinished event data
     */
    private void testRunFinished(TestRunFinished event) {
        try {
            logPrint("Finalizing Run: " + runData.uuid);
            runData.status = runData.getOverallStatus();
            runData.endDate = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();

            logPrint("Report Live: " + reportLive);
            if (reportLive) {
                logPrint("Updating Run");
                cedarApi.updateRun(runData);
            }
            if (bulkUpdate) {
                logPrint("Bulk Updating Cedar");
                cedarApi.bulkUpdateTestCases(runData.featureDataList);
            }
            logPrint("Run Finished");
        } catch (Exception e) {
            logPrint("EXCEPTION On Run Finished: " + e.toString() + "\n" + getStackTrace(e));
        }
    }

    /**
     * Cucumber logging helper method to write to log file with timestamp.
     *
     * @param message Message to be written to log file
     */
    private void logPrint(String message) {
        out.println(Instant.now().toString() + ":" + message);
    }

    /**
     * Helper method to set the appiumDriver used for the session being reported on. Appium Driver
     * is required to acquire pertinent information about the test environment at run time.
     *
     * @param appiumDriver Driver to be set.
     */
    public static void setAppiumDriver(AppiumDriver appiumDriver) {
        System.out.println("Session Driver Set");
        sessionAppiumDriver = appiumDriver;
    }

    /**
     * Method used to set the reason for failure (RFF) from an external source. Context of failure
     * may not be available to the scope of the test, so may need to be set from external
     * inspections.
     *
     * @param reasonForFailure The RFF key to lookup and set as the RFF.
     */
    public static void setFailureReason(String reasonForFailure) {
        externalRFF = reasonForFailure;
    }
}
