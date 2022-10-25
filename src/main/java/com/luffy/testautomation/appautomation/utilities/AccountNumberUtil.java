package com.luffy.testautomation.appautomation.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountNumberUtil {

    public static String getAccountNumberByPattern(String stringToBeMatched, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(stringToBeMatched);

        String matchedString = null;
        if (matcher.find()) {
            matchedString = matcher.group();
        }

        return matchedString;
    }

    public static String getSixDigitOTP(String text) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(text);
        String otp = matcher.find() ? matcher.group() : null;

        return otp;
    }
}
