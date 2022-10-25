package com.luffy.testautomation.utils.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineExecutor implements Shell {

    private static final Logger log = LoggerFactory.getLogger(CommandLineExecutor.class);

    private static String stringValueOf(ByteArrayOutputStream outAndErr)
            throws UnsupportedEncodingException {
        return outAndErr.toString("UTF-8").trim();
    }

    @Override
    public String execute(String command) throws IOException {
        ByteArrayOutputStream outAndErr = new ByteArrayOutputStream();
        try {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outAndErr);
            executor.setStreamHandler(streamHandler);
            log.info("command = " + command);
            executor.execute(CommandLine.parse(command));
            return stringValueOf(outAndErr);
        } catch (ExecuteException e) {
            throw new IOException("Command failed: " + command + "\n" + stringValueOf(outAndErr));
        } catch (IOException e) {
            throw new IOException(
                    "Command failed: '" + command + "', perhaps it is not installed?", e);
        }
    }

    public String executePipedCommand(String pipedCommand) throws IOException {
        String removeNewLineChars = " | tr -d '\\t\\n\\r'";
        ProcessBuilder processBuilder =
                new ProcessBuilder("/bin/bash", "-c", pipedCommand + removeNewLineChars);
        log.info("command = " + pipedCommand);
        return IOUtils.toString(processBuilder.start().getInputStream(), Charset.defaultCharset());
    }
}
