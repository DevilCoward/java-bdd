package com.luffy.testautomation.prjhelpers.LocaleHelpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocaleHelper {

    public String formatDateOfBirth(String dateOfBirth) throws Exception {
        String configDateFormat = "dd-MM-yyyy";

        String outputDateFormat = "MMM d yyyy";
        String frOutputDateFormat = "d MMM yyyy";
        String mxOutputDateFormat = "MMM dd yyyy";

        String currentLocale = System.getProperty("locale");
        Locale locale = new Locale(currentLocale);

        DateFormat inputDateFormat = new SimpleDateFormat(configDateFormat);
        Date inputDate = inputDateFormat.parse(dateOfBirth);

        if (currentLocale.equalsIgnoreCase("fr")) {
            outputDateFormat = frOutputDateFormat;
        } else if (currentLocale.equalsIgnoreCase("es")) {
            outputDateFormat = mxOutputDateFormat;
        }

        DateFormat outputDateFormatted = new SimpleDateFormat(outputDateFormat, locale);
        return outputDateFormatted.format(inputDate);
    }
}
