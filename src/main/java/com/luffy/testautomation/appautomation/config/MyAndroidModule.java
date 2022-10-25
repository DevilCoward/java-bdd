package com.luffy.testautomation.appautomation.config;

import com.google.common.collect.ImmutableList;
import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

// TODO: Doesn't seems like we need this to be an AbstractModule anymore. Possible Refactor here.
public class MyAndroidModule extends AbstractAndroidModule {

    private static final String country = System.getProperty("country").toLowerCase();
    private static final String osVersionCommand = "adb shell getprop ro.build.version.release";
    private static final String deviceNameCommand = "adb shell getprop ro.product.model";
    private static String appPackageCommand =
            "adb shell pm list packages | grep hsbc.hsbc | cut -d ':' -f2 | grep "
                    + country
                    + ".cert";
    public String deviceName = "Samsung Galaxy S8 Plus";

    public static final List<Pair<String, String>> deviceList =
            new ArrayList<>(
                    ImmutableList.of(
                            Pair.of("Oppo Reno 3 Pro", "10.0"),
                            Pair.of("HUAWEI P30", "9.0"),
                            Pair.of("VIVO Y50", "10.0"),
                            Pair.of("Samsung Galaxy J7 Prime", "8.1"),
                            Pair.of("Xiaomi Redmi Note 9", "10.0"),
                            Pair.of("Motorola Moto G9 Play", "10.0"),
                            Pair.of("Samsung Galaxy S8 Plus", "9.0"),
                            Pair.of("Samsung Galaxy S9 Plus", "9.0")));

    public static String osVersion() throws IOException {
        return execCommand(osVersionCommand);
    }

    public static String deviceName() throws IOException {
        return execCommand(deviceNameCommand);
    }

    public static String appPackageCap() throws IOException {
        if (country.equals("firstdirect")) {
            appPackageCommand = "adb shell pm list packages | grep fd.fduk | cut -d ':' -f2";
        }
        return new CommandLineExecutor().executePipedCommand(appPackageCommand);
    }

    public static String appActivity() {
        String country = Environment.requiredSystemProperty("country");
        if (country.equals("OPENBANKING")) {
            return "";
        } else if (country.equalsIgnoreCase("singapore")) {
            return "sg.com.hsbc.hsbcsingapore.cert.DefaultAppEntry";
        } else if (country.equalsIgnoreCase("hase")) {
            return "com.hangseng.rbmobile.activity.HSLaunchActivity";
        } else {
            return "com.hsbc.mobilebanking.prelogon.splash.SplashActivity";
        }
    }

    public static String appWaitActivity() {
        String country = Environment.requiredSystemProperty("country");
        if (country.equalsIgnoreCase("hase")) {
            return "com.hangseng.rbmobile.*";
        } else {
            return "com.hsbc.mobilebanking.*";
        }
    }

    /** Used to trigger terminal commands */
    public static String execCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream(), UTF_8));
        String matchingPlaceholder;
        StringBuilder result = new StringBuilder();
        while ((matchingPlaceholder = reader.readLine()) != null) {
            result.append(matchingPlaceholder).trimToSize();
        }
        return result.toString();
    }
}
