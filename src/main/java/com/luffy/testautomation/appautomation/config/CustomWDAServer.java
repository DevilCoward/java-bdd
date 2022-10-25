package com.luffy.testautomation.appautomation.config;

import com.luffy.testautomation.appautomation.utilities.Timedelta;
import com.luffy.testautomation.appautomation.utilities.UnixProcessHelpers;
import org.openqa.selenium.net.UrlChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Files.readLines;
import static com.luffy.testautomation.appautomation.config.MyIosModule.*;
import static com.luffy.testautomation.appautomation.utilities.UnixProcessHelpers.isProcessRunning;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class CustomWDAServer {

    private final Logger log = LoggerFactory.getLogger(CustomWDAServer.class);

    private final int MAX_REAL_DEVICE_RESTART_RETRIES = 1;
    private final Timedelta REAL_DEVICE_RUNNING_TIMEOUT = Timedelta.fromMinutes(4);
    private final Timedelta RESTART_TIMEOUT = Timedelta.fromMinutes(1);

    // These settings are needed to properly sign WDA for real device tests
    // See https://github.com/appium/appium-xcuitest-driver for more details
    private final File IPROXY_EXECUTABLE =
            new File(String.format("%s/%s", System.getProperty("user.home"), "/mobile/bin/iproxy"));
    private final File XCODEBUILD_EXECUTABLE = new File("/usr/bin/xcodebuild");
    private final File WDA_PROJECT =
            new File(
                    String.format(
                            "%s/mobile/WebDriverAgent/WebDriverAgent.xcodeproj",
                            System.getProperty("user.home")));
    private final String WDA_SCHEME = "WebDriverAgentRunner";
    private final String WDA_CONFIGURATION = "Debug";
    private final File XCODEBUILD_LOG =
            new File(
                    String.format(
                            "%s/mobile/WebDriverAgent/build.log", System.getProperty("user.home")));
    private final File IPROXY_LOG =
            new File(
                    String.format(
                            "%s/mobile/WebDriverAgent/iproxy.log",
                            System.getProperty("user.home")));

    private int port = 8100;
    private String SERVER_URL = String.format("http://127.0.0.1:%d", port);

    private final String[] IPROXY_CMDLINE =
            new String[] {
                IPROXY_EXECUTABLE.getAbsolutePath(),
                Integer.toString(port),
                Integer.toString(port),
                String.format("> %s 2>&1 &", IPROXY_LOG.getAbsolutePath())
            };

    private CustomWDAServer instance = null;
    private String deviceId;
    private int failedRestartRetriesCount = 0;

    public CustomWDAServer(String deviceId, int port) {
        this.deviceId = deviceId;
        this.SERVER_URL = String.format("http://127.0.0.1:%d", port);
    }

    public CustomWDAServer(String deviceId, String ipAddress, int port) {
        this.deviceId = deviceId;
        this.SERVER_URL = String.format("http://%s:%d", ipAddress, port);
    }

    private CustomWDAServer() {
        try {
            if (MyIosModule.isRealDevice) {
                this.deviceId = MyIosModule.deviceUdId();
            } else {
                this.deviceId = getId();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ensureToolsExistence();
        ensureParentDirExistence();
    }

    public synchronized CustomWDAServer getInstance() {
        if (instance == null) {
            instance = new CustomWDAServer();
        }
        return instance;
    }

    private boolean waitUntilIsRunning(Timedelta timeout) throws Exception {
        final URL status = new URL(SERVER_URL + "/status");
        try {
            if (timeout.asSeconds() > 5) {
                log.debug(
                        String.format(
                                "Waiting max %s until WDA server starts responding...", timeout));
            }
            new UrlChecker()
                    .waitUntilAvailable(timeout.asMilliSeconds(), TimeUnit.MILLISECONDS, status);
            return true;
        } catch (UrlChecker.TimeoutException e) {
            return false;
        }
    }

    private void ensureParentDirExistence() {
        if (!XCODEBUILD_LOG.getParentFile().exists()) {
            if (!XCODEBUILD_LOG.getParentFile().mkdirs()) {
                throw new IllegalStateException(
                        String.format(
                                "The script has failed to create '%s' folder for Appium logs. "
                                        + "Please make sure your account has correct access permissions on the parent folder(s)",
                                XCODEBUILD_LOG.getParentFile().getAbsolutePath()));
            }
        }
    }

    private void ensureToolsExistence() {
        if (MyIosModule.isRealDevice && !IPROXY_EXECUTABLE.exists()) {
            throw new IllegalStateException(
                    String.format(
                            "%s tool is expected to be installed (`npm install -g iproxy`)",
                            IPROXY_EXECUTABLE.getAbsolutePath()));
        }
        if (!XCODEBUILD_EXECUTABLE.exists()) {
            throw new IllegalStateException(
                    String.format(
                            "xcodebuild tool is not detected on the current system at %s",
                            XCODEBUILD_EXECUTABLE.getAbsolutePath()));
        }
        if (!WDA_PROJECT.exists()) {
            throw new IllegalStateException(
                    String.format(
                            "WDA project is expected to exist at %s",
                            WDA_PROJECT.getAbsolutePath()));
        }
    }

    // read from provisioning.txt
    public List<String> retrieveProvisioningProfile() {
        List<String> provisioningDetails = new ArrayList<>();
        try {
            provisioningDetails =
                    readLines(
                            new File(
                                    String.format(
                                            "%s/src/main/resources/provisioning.txt",
                                            System.getProperty("user.dir"))),
                            Charset.forName("utf-8"));
        } catch (Exception e) {
            log.info(getStackTrace(e));
        }
        return provisioningDetails;
    }

    private List<String> generateXcodebuildCmdline() {
        final List<String> result = new ArrayList<>();
        result.add(XCODEBUILD_EXECUTABLE.getAbsolutePath());
        result.add("clean build test");
        result.add(String.format("-project %s", WDA_PROJECT.getAbsolutePath()));
        result.add(String.format("-scheme %s", WDA_SCHEME));
        result.add(String.format("-destination id=%s", deviceId));
        result.add(String.format("-configuration %s", WDA_CONFIGURATION));
        result.add("DEVELOPMENT_TEAM=\"" + retrieveProvisioningProfile().get(1) + "\"");
        result.add("PROVISIONING_PROFILE=" + retrieveProvisioningProfile().get(0));
        result.add("PRODUCT_BUNDLE_IDENTIFIER=\"" + retrieveProvisioningProfile().get(2) + "\"");
        result.add(String.format("IPHONEOS_DEPLOYMENT_TARGET=%s", platformVersion));
        result.add("CODE_SIGN_STYLE=Manual");
        result.add(String.format("> %s 2>&1 &", XCODEBUILD_LOG.getAbsolutePath()));
        return result;
    }

    public synchronized void restart() throws Exception {
        if (MyIosModule.isRealDevice
                && failedRestartRetriesCount >= MAX_REAL_DEVICE_RESTART_RETRIES) {
            throw new IllegalStateException(
                    String.format(
                            "WDA server cannot start on the connected device with udid %s after %s retries. "
                                    + "Reboot the device manually and try again",
                            deviceId, MAX_REAL_DEVICE_RESTART_RETRIES));
        }

        final String hostname = InetAddress.getLocalHost().getHostName();
        log.info(String.format("Trying to (re)start WDA server on %s:%s...", hostname, port));
        UnixProcessHelpers.killProcessesGracefully(
                IPROXY_EXECUTABLE.getName(), XCODEBUILD_EXECUTABLE.getName());

        final File scriptFile = File.createTempFile("script", ".sh");
        try {
            final List<String> scriptContent = new ArrayList<>();
            scriptContent.add("#!/bin/bash");
            if (MyIosModule.isRealDevice) {
                scriptContent.add(String.join(" ", IPROXY_CMDLINE));
            }
            final String wdaBuildCmdline = String.join(" ", generateXcodebuildCmdline());
            log.debug(String.format("Building WDA with command line:\n%s\n", wdaBuildCmdline));
            scriptContent.add(wdaBuildCmdline);
            try (Writer output = Files.newBufferedWriter(scriptFile.toPath(), UTF_8)) {
                output.write(String.join("\n", scriptContent));
            }
            new ProcessBuilder("/bin/chmod", "u+x", scriptFile.getCanonicalPath())
                    .redirectErrorStream(true)
                    .start()
                    .waitFor(5, TimeUnit.SECONDS);
            final ProcessBuilder pb =
                    new ProcessBuilder("/bin/bash", scriptFile.getCanonicalPath());
            final Map<String, String> env = pb.environment();
            // This is needed for Jenkins
            env.put("BUILD_ID", "dontKillMe");
            // This line is important. If USE_PORT environment variable is not set then WDA
            // takes port number zero by default and won't accept any incoming requests
            env.put("USE_PORT", Integer.toString(port));
            log.info(
                    String.format(
                            "Waiting max %s for WDA to be (re)started on %s:%s...",
                            RESTART_TIMEOUT.toString(), hostname, port));
            final Timedelta started = Timedelta.now();
            pb.redirectErrorStream(true)
                    .start()
                    .waitFor(RESTART_TIMEOUT.asMilliSeconds(), TimeUnit.MILLISECONDS);
            if (!waitUntilIsRunning(RESTART_TIMEOUT)) {
                ++failedRestartRetriesCount;
                throw new IllegalStateException(
                        String.format(
                                "WDA server has failed to start after %s timeout on server '%s'.\n"
                                        + "Please make sure that iDevice is properly connected and you can build "
                                        + "WDA manually from XCode.\n"
                                        + "Xcodebuild logs:\n\n%s\n\n\niproxy logs:\n\n%s\n\n\n",
                                RESTART_TIMEOUT,
                                hostname,
                                getLog(XCODEBUILD_LOG).orElse("EMPTY"),
                                getLog(IPROXY_LOG).orElse("EMPTY")));
            }

            log.info(
                    String.format(
                            "WDA server has been successfully (re)started after %s "
                                    + "and now is listening on %s:%s",
                            Timedelta.now().diff(started).toString(), hostname, port));
        } finally {
            scriptFile.delete();
        }
    }

    public boolean isRunning() throws Exception {
        if (!isProcessRunning("xcodebuild")
                || (MyIosModule.isRealDevice && !isProcessRunning("iproxy"))) {
            return false;
        }
        return waitUntilIsRunning(
                MyIosModule.isRealDevice ? REAL_DEVICE_RUNNING_TIMEOUT : Timedelta.fromSeconds(3));
    }

    public Optional<String> getLog(File logFile) {
        if (logFile.exists()) {
            try {
                return Optional.of(
                        new String(readAllBytes(logFile.toPath()), Charset.forName("UTF-8")));
            } catch (IOException e) {
                log.info(getStackTrace(e));
            }
        }
        return Optional.empty();
    }

    private Map<String, String> simIdsMapping = new HashMap<>();

    public String getId() throws Exception {
        if (!simIdsMapping.containsKey(deviceName)) {
            final String output = simList();
            simIdsMapping.put(
                    deviceName,
                    parseSimulatorId(output, deviceName, platformVersion)
                            .orElseThrow(
                                    () ->
                                            new IllegalStateException(
                                                    String.format(
                                                            "Cannot get an id for %s (%s) Simulator from simctl output:\n%s",
                                                            deviceName, platformVersion, output))));
        }
        return simIdsMapping.get(deviceName);
    }

    private final String SUBSECTION_MARKER = "--";
    private final String SECTION_MARKER = "==";
    private final String DEVICES_SECTION_START = SECTION_MARKER + " Devices " + SECTION_MARKER;
    private final Function<String, String> IOS_SECTION_START_TEMPLATE =
            version -> String.format(SUBSECTION_MARKER + " iOS %s " + SUBSECTION_MARKER, version);
    private final Pattern ID_PATTERN =
            Pattern.compile(".*([\\w]{8}\\-[\\w]{4}\\-[\\w]{4}\\-[\\w]{4}\\-[\\w]{12}).*");

    private Optional<String> parseSimulatorId(
            String simctlOutput, String deviceName, String platformVersion) {
        final String subSectionStartMarker = IOS_SECTION_START_TEMPLATE.apply(platformVersion);
        boolean isInDeviceSection = false;
        boolean isInIOSSubsection = false;
        for (String line : simctlOutput.split("\n", -1)) {
            if (line.startsWith(DEVICES_SECTION_START)) {
                isInDeviceSection = true;
            } else if (isInDeviceSection && line.startsWith(SECTION_MARKER)) {
                isInDeviceSection = false;
            }
            if (isInDeviceSection && line.startsWith(subSectionStartMarker)) {
                isInIOSSubsection = true;
            } else if (isInIOSSubsection
                    && (line.startsWith(SUBSECTION_MARKER) || line.startsWith(SECTION_MARKER))) {
                isInIOSSubsection = false;
            }
            if (isInIOSSubsection && line.trim().startsWith(deviceName)) {
                final Matcher m = ID_PATTERN.matcher(line);
                if (m.find()) {
                    return Optional.of(m.group(1));
                }
            }
        }
        return Optional.empty();
    }

    public String getSERVER_URL() {
        return SERVER_URL;
    }

    public int getPort() {
        return port;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
