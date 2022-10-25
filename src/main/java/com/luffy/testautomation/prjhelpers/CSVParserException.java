package com.luffy.testautomation.prjhelpers;

/** {@code CSVParserException} is a sub-class of {@link RuntimeException} */
public class CSVParserException extends RuntimeException {
    public static String missingValueMessage =
            "key or value is missing in csv file or is not recognised";

    /**
     * Constructs a new csv parser exception with the specified detail message.
     *
     * @param message is the detailed message
     */
    public CSVParserException(String message) {
        super(message);
    }
}
