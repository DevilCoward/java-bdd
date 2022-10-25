package com.luffy.testautomation.prjhelpers;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.WebElement;

public class CharHelpers {
    public static char generateRandomChar() {
        Random random = new Random();
        int n = random.nextInt(69) + 32;
        if (n > 96) {
            n += 25;
        }
        return (char) n;
    }

    public static String generateRandomLetters(String upperOrlowerOrany) {
        Random random = new Random();
        char c = (char) (random.nextInt(26) + 'a');
        switch (upperOrlowerOrany) {
            case "upper":
                return String.valueOf(c).toUpperCase();
            case "lower":
                return String.valueOf(c).toLowerCase();
            default:
                return String.valueOf(c);
        }
    }

    public static boolean checkOrderOfWebElementsIos(List<WebElement> listName) {
        boolean returnValue = true;
        String previousElement = listName.get(0).getAttribute("label");
        for (int i = 1; i < listName.size(); i++) {
            String currentElement = listName.get(i).getAttribute("label");
            if (previousElement.compareToIgnoreCase(currentElement) > 0) {
                returnValue = false;
            }
            previousElement = currentElement;
        }
        return returnValue;
    }

    public static boolean checkOrderOfWebElementsAndroid(List<WebElement> listName) {
        boolean returnValue = true;
        String previousElement = listName.get(0).getText();
        for (int i = 1; i < listName.size(); i++) {
            String currentElement = listName.get(i).getText();
            if (previousElement.compareToIgnoreCase(currentElement) > 0) {
                returnValue = false;
            }
            previousElement = currentElement;
        }
        return returnValue;
    }

    public String getRandomString(String regex, int len) {
        StringBuilder temp = new StringBuilder();
        while (temp.length() < len) {
            String rnd = String.valueOf(generateRandomChar());
            if (Pattern.matches(regex, rnd)) {
                temp.append(rnd);
            }
        }
        return temp.toString();
    }

    public static boolean alpahbeticallyOrderedStringList(ArrayList<String> stringArrayList1) {
        boolean returnValue = false;
        ArrayList<String> stringArrayList2 = (ArrayList<String>) stringArrayList1.clone();
        Collections.sort(stringArrayList2);
        if (stringArrayList2.equals(stringArrayList1)) {
            returnValue = true;
        }
        return returnValue;
    }

    public static List<String> splitter(String mobileElement) {
        return Splitter.onPattern("\\r?\\n").splitToList(mobileElement);
    }

    /**
     * Use regex matcher to check whether regexStr and targetStr match
     *
     * @param regexStr String the regex str
     * @param targetStr the target string
     * @return true or false
     */
    public static boolean compileStr(String regexStr, String targetStr) {
        Matcher matcher = Pattern.compile(regexStr).matcher(targetStr);
        return matcher.matches();
    }

    /**
     * to check regex is match in list of WebElement text
     *
     * @param elements List<WebElement> get the elements on the view
     * @param regex String the regex str
     * @return true or false
     */
    public static boolean isMatchInElements(List<WebElement> elements, String regex) {
        return elements.stream().anyMatch(webElement -> compileStr(regex, webElement.getText()));
    }
}
