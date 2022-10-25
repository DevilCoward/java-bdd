package com.luffy.testautomation.appautomation.config;

import com.google.common.collect.ImmutableList;
import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

// TODO: Doesn't seems like we need this to be an AbstractModule anymore. Possible Refactor here.
public class MyIosModule extends AbstractIosModule {

    private static final Logger log = LoggerFactory.getLogger(MyIosModule.class);
    private static final String realDeviceUdidCommand = "idevice_id -l";
    private static final String simulatorUdidCommand =
            "xcrun simctl list | grep -i booted | head -n 1 | cut -d '(' -f2 | cut -d ')' -f1";
    private static final String simListCommand = "xcrun simctl list";
    private static final String bundleIdScript = "scripts/extract-bundleId-from-ipa.bash";
    private static final String realDeviceModel =
            "ios-deploy -c | tail -n 1 | cut -d '(' -f2 | cut -d ',' -f2";
    private static final String simulatorModel =
            "xcrun simctl list | grep -i booted | head -n 1 | cut -d '(' -f1";
    public static String deviceName = "iPhone XS";
    public static String platformVersion = "";
    public static String bundleId;
    public static boolean isRealDevice;

    static {
        try {
            isRealDevice = !execCommand(realDeviceUdidCommand).isEmpty();
        } catch (IOException ignored) {
        }
    }

    public static List<Pair<String, String>> deviceList =
            new ArrayList<>(
                    ImmutableList.of(
                            Pair.of("iPhone XS", "13.0"),
                            Pair.of("iPhone XS", "14.0"),
                            Pair.of("iPhone 11", "13.0"),
                            Pair.of("iPhone 11 Pro", "13.0"),
                            Pair.of("iPhone 11", "14.0"),
                            Pair.of("iPhone 12 Pro Max", "14.0"),
                            Pair.of("iPhone 8", "13.0"),
                            Pair.of("iPhone SE 2020", "13.0"),
                            Pair.of("iPad Air 4", "14.0"),
                            Pair.of("iPhone 12 Mini", "14.0")));

    public static String getBundleId(String appPath) {
        String simBundleId = null;
        String pathToScript = System.getProperty("user.dir") + "/scripts/getBundleId.sh ";
        String command = "sh " + pathToScript + appPath;
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader read =
                    new BufferedReader(new InputStreamReader(proc.getInputStream(), UTF_8));
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            while (read.ready()) {
                simBundleId = read.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get Bundle Id");
        }
        String bundleId = "";
        if (isRealDevice) {
            try {
                bundleId = new CommandLineExecutor().execute(bundleIdScript + " " + appPath);
            } catch (IOException error) {
                throw new RuntimeException("Failed to get bundle Id of iOS ipa", error);
            }
        } else {
            bundleId = simBundleId;
        }
        return bundleId;
    }

    public static String deviceModel() throws IOException {
        String simulatorName = execCommand(simulatorModel).trim();
        if (isRealDevice) {
            log.info("******Connected to iOS real device******");
            return execCommand(realDeviceModel).trim();
        } else if (!simulatorName.isEmpty()) {
            log.info("******Connected to iOS Simulator******");
            return simulatorName;
        }
        log.info("******There is NO any iOS real device or Simulator detected******");
        return null;
    }

    public static String deviceUdId() throws IOException {
        if (isRealDevice) {
            return execCommand(realDeviceUdidCommand).trim();
        } else {
            return execCommand(simulatorUdidCommand).trim();
        }
    }

    public static String simList() throws IOException {
        return execCommand(simListCommand);
    }

    public static String execCommand(String command) throws IOException {
        if (command.contains("|")) {
            return new CommandLineExecutor().executePipedCommand(command);
        } else {
            return new CommandLineExecutor().execute(command);
        }
    }
}
