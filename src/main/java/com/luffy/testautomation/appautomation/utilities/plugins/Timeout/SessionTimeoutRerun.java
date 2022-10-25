package com.luffy.testautomation.appautomation.utilities.plugins.Timeout;

import cucumber.api.SummaryPrinter;
import cucumber.api.event.*;
import cucumber.api.formatter.NiceAppendable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SessionTimeoutRerun implements ConcurrentEventListener, SummaryPrinter {
    private final NiceAppendable out;
    private static final Path overrunFileName =
            Paths.get(
                    System.getProperty("user.dir"),
                    "build",
                    "reports",
                    "tests",
                    "results",
                    "overrun_tests.txt");
    private static final Path previousRunFileName =
            Paths.get(
                    System.getProperty("user.dir"),
                    "build",
                    "reports",
                    "tests",
                    "results",
                    "previous_run.txt");
    private static final Path rerunSkippedFileName = Paths.get("rerun_skipped.txt");
    private static List<String> skippedThisSession = new ArrayList<>();
    private static Instant timeout;

    public SessionTimeoutRerun(Appendable out) {
        this.out = new NiceAppendable(out);
        timeout = Instant.now().plus(90, ChronoUnit.MINUTES);
    }

    public SessionTimeoutRerun() {
        this.out = null;
        timeout = Instant.now().plus(90, ChronoUnit.MINUTES);
    }

    /**
     * Called to associate methods to be called for each event
     *
     * @param publisher EventPublisher that handles all events in the framework
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::testCaseStarted);
        publisher.registerHandlerFor(TestRunStarted.class, this::testRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::testRunFinished);
    }

    /**
     * Called before execution of tests but after each source-read and step-defined
     *
     * @param event TestRunStarted event data
     */
    private void testRunStarted(TestRunStarted event) {
        logPrint("Cleaning previous rerun");
        // remove previous rerun file
        File outputFile = new File(rerunSkippedFileName.toAbsolutePath().toString());
        outputFile.delete();
    }

    private void testCaseStarted(TestCaseStarted event) {
        String caseUri = event.testCase.getUri();
        String scenarioDesignation = event.testCase.getScenarioDesignation();

        logPrint("Checking Timeout: " + caseUri);
        boolean timeoutReached = Instant.now().getEpochSecond() > timeout.getEpochSecond();
        logPrint(
                "Current Epoch: "
                        + Instant.now().getEpochSecond()
                        + " > "
                        + timeout.getEpochSecond()
                        + ": "
                        + timeoutReached);

        if (timeoutReached) {
            logPrint("Writing timeout for: " + caseUri);
            skippedThisSession.add(caseUri);
            saveTest(caseUri, overrunFileName);
            saveTest(scenarioDesignation, rerunSkippedFileName);
        } else {
            saveTest(caseUri, previousRunFileName);
        }
    }

    /**
     * Called after execution of all tests
     *
     * @param event TestRunFinished event data
     */
    private void testRunFinished(TestRunFinished event) {
        logPrint("Skipped this session: " + skippedThisSession.size());
    }

    /**
     * Write test information out to a file
     *
     * @param caseUri Case URI to be written
     * @param saveTo File to write to
     */
    private void saveTest(String caseUri, Path saveTo) {
        try {

            PrintWriter out =
                    new PrintWriter(Files.newBufferedWriter(saveTo, UTF_8, CREATE, APPEND));
            out.println(caseUri);
            out.close();
        } catch (IOException exception) {
            logPrint("IO EXCEPTION: " + exception);
        }
    }

    /**
     * Plugin log file writer. Prepends current time before writing message to log file
     *
     * @param message Message to be printed to the log
     */
    private void logPrint(String message) {
        if (out != null) {
            out.println(Instant.now().toString() + ":" + message);
        }
    }
}
