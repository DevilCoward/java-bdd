package com.luffy.testautomation.utils.helpers;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptional {

    public static String asString(Throwable e) {
        StringWriter writer = new StringWriter();
        try (PrintWriter w = new PrintWriter(writer)) {
            e.printStackTrace(w);
        }
        return writer.toString();
    }

    public static String describeCauseOf(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        while (hasCause(e)) {
            e = e.getCause();
            sb.append("; because ");
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public static Throwable rootCauseOf(Throwable e) {
        Throwable cause = e;
        while (hasCause(cause)) {
            cause = cause.getCause();
        }
        return cause;
    }

    private static boolean hasCause(Throwable e) {
        return e.getCause() != null && e.getCause() != e;
    }
}
