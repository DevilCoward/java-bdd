package com.luffy.testautomation.appautomation.utilities;

import com.luffy.testautomation.utils.helpers.CommandLineExecutor;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UnixProcessHelpers {
    private static final Logger log = LoggerFactory.getLogger(UnixProcessHelpers.class);

    private static final int DEFAULT_COMMAND_TIMEOUT_SECONDS = 5;

    public static void killProcessesGracefully(String... expectedNames) throws Exception {
        killProcessesGracefully(DEFAULT_COMMAND_TIMEOUT_SECONDS, expectedNames);
    }

    private static String execCommand(String command) throws IOException {
        if (command.contains("|")) {
            return new CommandLineExecutor().executePipedCommand(command);
        } else {
            return new CommandLineExecutor().execute(command);
        }
    }

    public static boolean isProcessRunning(String name) {
        try {
            execCommand("pgrep " + name);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void killProcessesGracefully(
            int timeoutSecondsUntilForceKill, String... expectedNames) throws Exception {
        new ProcessBuilder(ArrayUtils.addAll(new String[] {"/usr/bin/killall"}, expectedNames))
                .redirectErrorStream(true)
                .start()
                .waitFor(5, TimeUnit.SECONDS);
        final long millisecondsStarted = System.currentTimeMillis();
        boolean areAllProcessesTerminated = true;
        while (System.currentTimeMillis() - millisecondsStarted
                <= timeoutSecondsUntilForceKill * 1000) {
            final Process p =
                    new ProcessBuilder("/bin/ps", "axco", "command")
                            .redirectErrorStream(true)
                            .start();
            final InputStream stream = p.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                for (String name : expectedNames) {
                    if (line.matches(".*\\b" + name.trim() + "\\b.*")) {
                        areAllProcessesTerminated = false;
                        break;
                    }
                }
                if (areAllProcessesTerminated) {
                    break;
                }
            }
            if (areAllProcessesTerminated) {
                break;
            } else {
                Thread.sleep(300);
            }
        }
        if (!areAllProcessesTerminated) {
            log.warn(
                    String.format(
                            "Not all processes managed to stop gracefully in %s seconds. Force killing of %s...",
                            timeoutSecondsUntilForceKill, Arrays.toString(expectedNames)));
            new ProcessBuilder(
                            ArrayUtils.addAll(
                                    new String[] {"/usr/bin/killall", "-9"}, expectedNames))
                    .redirectErrorStream(true)
                    .start()
                    .waitFor(1, TimeUnit.SECONDS);
        }
    }
}
