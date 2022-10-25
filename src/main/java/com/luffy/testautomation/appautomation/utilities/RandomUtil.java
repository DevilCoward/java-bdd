package com.luffy.testautomation.appautomation.utilities;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private static final String SPECIAL_CHARS = "!#$%&'()*+,-./:;=?@[]^_`{}~";
    private static final String INVALID_CHARS = "<>|";
    private static final String NUMBER_FORMAT_EXP = "#.";
    private static final String NUMBER_FORMAT_DECIMAL_EXP = "0";

    private RandomUtil() {}

    public static String generateSpecialCharacterText(final int length) {
        char[] specialChars = RandomUtil.SPECIAL_CHARS.toCharArray();
        return RandomStringUtils.random(length, 0, specialChars.length, false, false, specialChars);
    }

    public static String generateAlphaNumericText(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String generateIntNumber(final int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static int generateIntNumber(final int start, final int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    public static double generateDoubleNumber(final int start, final int end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }

    public static String generateDoubleNumber(
            final int start, final int end, final int decimalPlaces) {
        double number = generateDoubleNumber(start, end);
        return formatDouble(number, getDecimalFormat(decimalPlaces));
    }

    public static String getDecimalFormat(final int decimalPlaces) {
        return new StringBuilder(RandomUtil.NUMBER_FORMAT_EXP)
                .append(StringUtils.repeat(RandomUtil.NUMBER_FORMAT_DECIMAL_EXP, decimalPlaces))
                .toString();
    }

    public static String formatDouble(final double number, final String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    public static String generateAlphabatic(final int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String generateSingleRandomAlphabet() {
        return RandomStringUtils.randomAlphabetic(1);
    }

    public static String generateInvalidCharacterText(final int length) {
        char[] specialChars = RandomUtil.INVALID_CHARS.toCharArray();
        return RandomStringUtils.random(length, 0, specialChars.length, false, false, specialChars);
    }
}
