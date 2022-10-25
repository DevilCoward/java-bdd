package com.luffy.testautomation.prjhelpers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.google.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyHelpers {
    private static final Logger log = LoggerFactory.getLogger(MoneyHelpers.class);

    @Inject
    public MoneyHelpers() {}

    /**
     * Used to convert currency code to how iOS will read it (Due to accessibility) This does the
     * opposite of currencyAsReadableIosString(String currencyToGet)
     *
     * <p>Background: Ex. iOS will show "CAD" but label will be "Canadian dollar", Appium can only
     * find "Canadian dollar"
     *
     * @param currencyCode String. Country's currency code - ex. CAD
     * @return String. IOS string for currency code - ex. Canadian dollar
     */
    public static String convertCurrencyCodeToIosReadableString(String currencyCode) {
        Map<String, String> currencyToIosStringMap = new HashMap<>();

        if (System.getProperty("locale").equalsIgnoreCase("en")) {
            currencyToIosStringMap.put("CAD", "Canadian dollar");
        } else if (System.getProperty("locale").equalsIgnoreCase("fr")) {
            currencyToIosStringMap.put("CAD", "dollars canadiens");
        }
        return currencyToIosStringMap.get(currencyCode);
    }

    public static String currencyAsReadableIosString(String currencyToGet) {
        Map<String, String> currencyToGetAsAString = new HashMap<>();

        if (System.getProperty("locale").equalsIgnoreCase("en")
                || System.getProperty("locale").equalsIgnoreCase("el")
                || System.getProperty("locale").equalsIgnoreCase("ar")) {
            currencyToGetAsAString.put("CAD", "\\d+ (Canadian dollar)s?( and .*)?");
            currencyToGetAsAString.put("GBP", "\\d+ (British pound)s?( and .*)?");
            currencyToGetAsAString.put("EUR", "\\d+ (euro)( and .*)?");
            currencyToGetAsAString.put("USD", "\\d+ (U S dollar)s?( and .*)?");
            currencyToGetAsAString.put("CNY", "\\d+ (C N Y)( and .*)?");
            currencyToGetAsAString.put("BMD", ".* (B M D)");
            currencyToGetAsAString.put("QAR", ".* (Q A R)");
        } else if (System.getProperty("locale").equalsIgnoreCase("fr")) {
            currencyToGetAsAString.put("CAD", "\\d+ (dollars canadiens)( et .*)?");
            currencyToGetAsAString.put("GBP", "\\d+ (livres brittaniques)( et .*)?");
            currencyToGetAsAString.put("EUR", "\\d+ (euros)( et .*)?");
            currencyToGetAsAString.put("USD", "\\d+ (dollars américain)( et .*)?");
            currencyToGetAsAString.put("CNY", "\\d+ (C N Y)( et .*)?");
        }

        AtomicReference<String> currencySymbol = new AtomicReference<>();
        currencySymbol.set("No currency match");
        try {
            String match =
                    currencyToGetAsAString.values().stream()
                            .filter(currencyToGet::matches)
                            .collect(Collectors.toList())
                            .get(0);
            currencyToGetAsAString.forEach(
                    (key, value) -> {
                        if (value.equalsIgnoreCase(match)) {
                            currencySymbol.set(key);
                        }
                    });
        } catch (IndexOutOfBoundsException e) {
            e.getMessage();
        }
        return currencySymbol.get();
    }

    public static Double parseAmount(String accountBalance) {
        Double amountValue;
        switch (System.getProperty("country").toLowerCase()) {
            case "greece":
                accountBalance =
                        accountBalance.replaceAll(",", "").replaceAll("[^\\d.\\W]", "").trim();
                amountValue = Double.parseDouble(accountBalance);
                break;
            case "uk":
            case "firstdirect":
                accountBalance = accountBalance.replaceAll("[a-zA-Z,:, £,\\s]", "");
                amountValue = Double.parseDouble(accountBalance);
                break;
            case "singapore":
                accountBalance = accountBalance.replaceAll("[^0-9.-]", "");
                amountValue = Double.parseDouble(accountBalance);
                break;
            case "ciiom":
                accountBalance = accountBalance.replaceAll("[a-zA-Z, £]", "");
                amountValue = Double.parseDouble(accountBalance);
                break;
            case "india":
                accountBalance = accountBalance.replaceAll("[^0-9.-]", "").replaceAll("\\s+", "");
                amountValue = Double.parseDouble(accountBalance);
                log.info(" amountValue : " + amountValue);
                break;
            case "armenia":
            case "qatar":
            case "oman":
            case "uae":
            case "bahrain":
                accountBalance = accountBalance.replaceAll("[a-zA-Z_, £]", "");
                amountValue = Double.parseDouble(accountBalance);
                break;
            case "china":
                accountBalance =
                        accountBalance
                                .substring(accountBalance.indexOf("¥") + 1)
                                .replaceAll(",", "");
                amountValue = Double.parseDouble(accountBalance);
                break;
            default:
                if (System.getProperty("locale").equals("fr")) {
                    accountBalance =
                            accountBalance.replaceAll("\\s|\\u00A0", "").replaceAll(",", ".");
                }
                accountBalance = accountBalance.trim();
                ArrayList<String> processedTokens = new ArrayList<String>();
                String[] accBalComponentStrArray = accountBalance.split("\\s+", -1);
                if (accBalComponentStrArray.length > 1) {
                    for (String accBalComponentStr : accBalComponentStrArray) {
                        if (Character.isDigit(accBalComponentStr.charAt(0))) {
                            accBalComponentStr = accBalComponentStr.replaceAll("[^0-9.]", "");
                            processedTokens.add(accBalComponentStr);
                        }
                    }
                    int processedTokensSize = processedTokens.size();
                    boolean expected = true;
                    boolean actual = processedTokensSize <= 2;
                    String reasonForFailure =
                            "ProcessedTokens value is invalid/greater than 2. Should be less than or equal to 2.";
                    assertThat(reasonForFailure, actual, equalTo(expected));
                    StringBuilder processedAccBalStrBuilder =
                            new StringBuilder(processedTokens.get(0));
                    if (processedTokensSize > 1) {
                        processedAccBalStrBuilder.append('.');
                        processedAccBalStrBuilder.append(processedTokens.get(1));
                    }
                    accountBalance = processedAccBalStrBuilder.toString();
                } else {
                    accountBalance = accountBalance.replaceAll("[^0-9.]", "");
                }
                amountValue = Double.parseDouble(accountBalance);
        }
        log.info("Double amountValue = " + amountValue);
        return amountValue;
    }

    public void selectFutureDate(int daysAheadToSelect, List<WebElement> datesRetrieved) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate futureDate = today.plusDays(daysAheadToSelect);
        String dateToPick = Integer.toString(futureDate.getDayOfMonth());
        datesRetrieved.stream()
                .filter(
                        date ->
                                date.getText().equals(dateToPick)
                                        && !date.getAttribute("value").contains("disabled"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Date not found"))
                .click();
    }

    public static double wordsToDigits(String numberToConvert) {
        long result = 0;
        String result1 = "";
        String result2 = "";
        long finalResult = 0;
        String beforePoint = "";
        String afterPoint = "";
        String[] splitBefore = new String[0];
        String[] splitAfter = new String[0];
        Boolean isNegative = false;

        List<String> allowedStrings =
                Arrays.asList(
                        "zero",
                        "one",
                        "two",
                        "three",
                        "four",
                        "five",
                        "six",
                        "seven",
                        "eight",
                        "nine",
                        "ten",
                        "eleven",
                        "twelve",
                        "thirteen",
                        "fourteen",
                        "fifteen",
                        "sixteen",
                        "seventeen",
                        "eighteen",
                        "nineteen",
                        "twenty",
                        "thirty",
                        "forty",
                        "fifty",
                        "sixty",
                        "seventy",
                        "eighty",
                        "ninety",
                        "hundred",
                        "thousand",
                        "million",
                        "billion",
                        "trillion");

        List<String> allowedStrings1 =
                Arrays.asList(
                        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
                        "nine");

        if (numberToConvert != null
                && numberToConvert.length() > 0
                && !numberToConvert.matches(".*\\d.*")) {
            numberToConvert = numberToConvert.replaceAll("-", " ");
            numberToConvert = numberToConvert.toLowerCase().replaceAll(" and", " ");

            if (numberToConvert.contains("point")) {
                String[] splittedParts = numberToConvert.trim().split("point", -1);

                beforePoint = splittedParts[0];
                afterPoint = splittedParts[1];
                splitBefore = beforePoint.trim().split("\\s");

                splitAfter = afterPoint.trim().split("\\s");
            } else {
                splitBefore = numberToConvert.trim().split(" ");
            }

            for (String str : splitBefore) {
                if (!allowedStrings.contains(str)) {
                    if (str.equals("minus")) {
                        isNegative = true;
                    }
                    log.info("Invalid word found : " + str);
                    numberToConvert = numberToConvert.replaceAll(str, "");

                } else {
                    {
                        if (str.equalsIgnoreCase("zero")) {
                            result += 0;
                        } else if (str.equalsIgnoreCase("one")) {
                            result += 1;
                        } else if (str.equalsIgnoreCase("two")) {
                            result += 2;
                        } else if (str.equalsIgnoreCase("three")) {
                            result += 3;
                        } else if (str.equalsIgnoreCase("four")) {
                            result += 4;
                        } else if (str.equalsIgnoreCase("five")) {
                            result += 5;
                        } else if (str.equalsIgnoreCase("six")) {
                            result += 6;
                        } else if (str.equalsIgnoreCase("seven")) {
                            result += 7;
                        } else if (str.equalsIgnoreCase("eight")) {
                            result += 8;
                        } else if (str.equalsIgnoreCase("nine")) {
                            result += 9;
                        } else if (str.equalsIgnoreCase("ten")) {
                            result += 10;
                        } else if (str.equalsIgnoreCase("eleven")) {
                            result += 11;
                        } else if (str.equalsIgnoreCase("twelve")) {
                            result += 12;
                        } else if (str.equalsIgnoreCase("thirteen")) {
                            result += 13;
                        } else if (str.equalsIgnoreCase("fourteen")) {
                            result += 14;
                        } else if (str.equalsIgnoreCase("fifteen")) {
                            result += 15;
                        } else if (str.equalsIgnoreCase("sixteen")) {
                            result += 16;
                        } else if (str.equalsIgnoreCase("seventeen")) {
                            result += 17;
                        } else if (str.equalsIgnoreCase("eighteen")) {
                            result += 18;
                        } else if (str.equalsIgnoreCase("nineteen")) {
                            result += 19;
                        } else if (str.equalsIgnoreCase("twenty")) {
                            result += 20;
                        } else if (str.equalsIgnoreCase("thirty")) {
                            result += 30;
                        } else if (str.equalsIgnoreCase("forty")) {
                            result += 40;
                        } else if (str.equalsIgnoreCase("fifty")) {
                            result += 50;
                        } else if (str.equalsIgnoreCase("sixty")) {
                            result += 60;
                        } else if (str.equalsIgnoreCase("seventy")) {
                            result += 70;
                        } else if (str.equalsIgnoreCase("eighty")) {
                            result += 80;
                        } else if (str.equalsIgnoreCase("ninety")) {
                            result += 90;
                        } else if (str.equalsIgnoreCase("hundred")) {
                            result *= 100;
                        } else if (str.equalsIgnoreCase("thousand")) {
                            result *= 1000;
                            finalResult += result;
                            result = 0;
                        } else if (str.equalsIgnoreCase("million")) {
                            result *= 1000000;
                            finalResult += result;
                            result = 0;
                        } else if (str.equalsIgnoreCase("billion")) {
                            result *= 1000000000;
                            finalResult += result;
                            result = 0;
                        } else if (str.equalsIgnoreCase("trillion")) {
                            result *= 1000000000000L;
                            finalResult += result;
                            result = 0;
                        }
                    }
                }
            }
            finalResult += result;
            if (isNegative) {
                finalResult *= -1;
            }
            for (String str1 : splitAfter) {
                if (!allowedStrings1.contains(str1)) {
                    log.info("Invalid word found : " + str1);
                    numberToConvert = numberToConvert.replaceAll(str1, "");

                } else {

                    if (str1.equalsIgnoreCase("zero")) {
                        result1 = result1.concat("0");
                    } else if (str1.equalsIgnoreCase("one")) {
                        result1 = result1.concat("1");
                    } else if (str1.equalsIgnoreCase("two")) {
                        result1 = result1.concat("2");
                    } else if (str1.equalsIgnoreCase("three")) {
                        result1 = result1.concat("3");
                    } else if (str1.equalsIgnoreCase("four")) {
                        result1 = result1.concat("4");
                    } else if (str1.equalsIgnoreCase("five")) {
                        result1 = result1.concat("5");
                    } else if (str1.equalsIgnoreCase("six")) {
                        result1 = result1.concat("6");
                    } else if (str1.equalsIgnoreCase("seven")) {
                        result1 = result1.concat("7");
                    } else if (str1.equalsIgnoreCase("eight")) {
                        result1 = result1.concat("8");
                    } else if (str1.equalsIgnoreCase("nine")) {
                        result1 = result1.concat("9");
                    }
                }
            }
            String stringBefore = String.valueOf(finalResult).concat(".");
            return Double.parseDouble(stringBefore.concat(result1));
        } else {
            ArrayList<String> st = new ArrayList<>();
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(numberToConvert);
            while (m.find()) {

                st.add(m.group());
            }

            result2 = st.get(0).concat(".").concat(st.get(1));
        }
        return Double.parseDouble(result2);
    }

    // Add to the map as and where required
    public static String currencyToSymbol(String symbolToRetrieve) {
        final Map<String, String> Currency_Map = new HashMap<String, String>();
        Currency_Map.put("USD", "$");
        Currency_Map.put("GBP", "£");
        Currency_Map.put("CAD", "$");
        Currency_Map.put("MXN", "$");
        Currency_Map.put("EUR", "€");

        for (Entry<String, String> entry : Currency_Map.entrySet()) {
            if (symbolToRetrieve.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    // eg. Use Regular Exp - "\\W\\d+.\\d+" for $1234.12 or "\\d+.\\d+\\s[a-zA-Z]{3}" for 1234.12
    // USD
    public static boolean currencyPatternMatcher(String myRegExpression, String stringToCheck) {
        return Pattern.matches(myRegExpression, stringToCheck);
    }

    // Used to get only the Amount value when the selector have more information than needed.
    // eg. From, High-Rate Savings, Account Number : 751-0**511-203, 798936 Canadian dollars and 37
    // cents
    public static String getBalanceFromAccountInfo(String amountInfo) {
        String[] accInfoInArray = amountInfo.split(",", -1);
        String accBalanceAsString = "";
        if (accInfoInArray.length > 3) {
            for (int i = 3; i < accInfoInArray.length; i++) {
                accBalanceAsString = accBalanceAsString.concat(accInfoInArray[i]);
            }
        }
        return accBalanceAsString;
    }

    // Used to get only the Amount value when the selector have more information than needed.
    // e.g Balance, 800,588.31
    public static String getBalanceFromAccountScreen(String amountLabel) {
        String[] amountLabelArray = amountLabel.split(",", -1);
        String accBalanceAsString = "";
        if (amountLabelArray.length > 1) {
            for (int i = 0; i < amountLabelArray.length; i++) {
                accBalanceAsString = accBalanceAsString.concat(amountLabelArray[i]);
            }
        }
        return accBalanceAsString;
    }

    // This method is used to parse amount as per locale
    public static BigDecimal getParseTransactionAmount(String transactionAmount, Locale locale)
            throws ParseException {
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(transactionAmount);
    }
}
