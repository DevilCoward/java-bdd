package com.luffy.testautomation.prjhelpers;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocaleCSVParser {
    private final Logger log = LoggerFactory.getLogger(LocaleCSVParser.class);

    private static HashMap<String, String> localeMap = new HashMap<String, String>();

    /**
     * This method searches through the map using the key to find the localized text
     *
     * @param key
     * @return the localized text value of the key
     */
    public static String getLocaleValue(String key) {
        String value = "";
        if (localeMap.get(key) != null) {
            value = localeMap.get(key);
        } else {
            value = CSVParserException.missingValueMessage;
        }
        return value;
    }

    /**
     * This method loads the localizable strings from the .csv file and creates a key-value map
     *
     * @throws ArrayIndexOutOfBoundsException if key or value is missing
     */
    public void createMapWithLocalisedValues() {
        char commentMarker = '/';
        String country = System.getProperty("country");
        String locale = System.getProperty("locale");
        String filePath = "src/main/resources/locales/" + country + "/" + locale + ".csv";

        try {
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            CSVParser csvParser =
                    new CSVParser(reader, CSVFormat.DEFAULT.withCommentMarker(commentMarker));

            for (CSVRecord csvRecord : csvParser) {
                String key = csvRecord.get(0);
                String value = csvRecord.get(1);
                if (key == null
                        || value == null
                        || key.equalsIgnoreCase("")
                        || value.equalsIgnoreCase("")) {
                    throw new CSVParserException(
                            CSVParserException.missingValueMessage + ": " + key);
                }
                localeMap.put(key, value);
            }
        } catch (IOException exception) {
            log.info(getStackTrace(exception));
        }
    }
}
