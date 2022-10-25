package com.luffy.testautomation.utils.helpers;

import com.google.common.base.Splitter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Adb {

    private final Shell executor;

    public Adb() {
        executor = new CommandLineExecutor();
    }

    public Adb(Shell executor) {
        this.executor = executor;
    }

    private static String adb() {
        return Optional.ofNullable(System.getenv("ANDROID_HOME"))
                .map(d -> new File(d, "platform-tools"))
                .map(d -> new File(d, "adb"))
                .map(File::getAbsolutePath)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Must define environment variable ANDROID_HOME"));
    }

    public Device device(Optional<String> selector) throws IOException {

        List<String> matched =
                devices().stream()
                        .filter(
                                selector.<Predicate<String>>map(v -> s -> s.contains(v))
                                        .orElse(s -> true))
                        .collect(Collectors.toList());

        switch (matched.size()) {
            case 0:
                throw new RuntimeException("No devices matched selector " + selector);
            case 1:
                return new Device(matched.get(0));
            default:
                throw new RuntimeException(
                        "Multiple devices" + matched + " matched selector '" + selector + "'");
        }
    }

    public Device only() {
        return new Device();
    }

    public List<String> devices() throws IOException {

        String output = executor.execute(adb() + " devices");

        return Splitter.on("\n").splitToList(output).stream()
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .filter(l -> !l.contains("List of devices"))
                .map(l -> Splitter.on("\t").splitToList(l).get(0))
                .collect(Collectors.toList());
    }

    public class Device {

        private final String additional;

        Device() {
            additional = "";
        }

        public Device(String serial) {
            additional = " -s " + serial;
        }

        public String osVersion() throws IOException {
            return executor.execute(target() + " shell getprop ro.build.version.release");
        }

        private String target() {
            return adb() + additional;
        }

        public String deviceName() throws IOException {
            return executor.execute(target() + " shell getprop ro.product.model");
        }

        public List<String> installedPackages() throws IOException {
            String output = executor.execute(target() + " shell pm list packages");
            return Splitter.on("\n").splitToList(output).stream()
                    .map(String::trim)
                    .filter(l -> !l.isEmpty())
                    .map(l -> Splitter.on(":").splitToList(l).get(1))
                    .collect(Collectors.toList());
        }
    }
}
