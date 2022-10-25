package com.luffy.testautomation.utils.helpers.actionHelpers;

import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.CustomKeys;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.openqa.selenium.WebElement;

public class ActionsAndroid extends Actions {

    @Inject
    ActionsAndroid(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver, appiumHelpers);
    }

    /**
     * With a keypad overlay on the screen, press the specified character on the keypad. Usage
     * includes keypads that do not accept normal text injection.
     *
     * @param character The character/number to be pressed.
     */
    @Override
    public void selectFromKeypad(char character) {
        switch (character) {
            case '0':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_0));
                break;
            case '1':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_1));
                break;
            case '2':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_2));
                break;
            case '3':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_3));
                break;
            case '4':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_4));
                break;
            case '5':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_5));
                break;
            case '6':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_6));
                break;
            case '7':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_7));
                break;
            case '8':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_8));
                break;
            case '9':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.DIGIT_9));
                break;
            case '.':
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.PERIOD));
                break;
            default:
                System.err.println("Character is not configured: " + character);
        }
    }

    /**
     * With a calendar overlay on the screen, select the specified date from the calendar. Date
     * format specified should be such that it can be parsed by java.time.LocalDate.parse().
     *
     * @param date The date to be selected.
     */
    @Override
    public void selectDateFromPicker(String date) {
        // FIXME: https://jira-digital.systems.uk.hsbc/jira/browse/MXGL-36053
        String datePickerHeaderId = "android:id/date_picker_header_year";
        String currentDateXpath = "//android.view.View[@checked='true']";
        String yearXpath = "//android.widget.TextView[@resource-id='android:id/text1']";
        String datePickerOkId = "android:id/button1";
        String monthArrow = "";
        LocalDate targetDate = LocalDate.parse(date);
        appiumHelpers.waitForElementById(datePickerHeaderId);
        String fullDateString =
                appiumHelpers.waitForElementByXpath(currentDateXpath).getAttribute("content-desc");
        DateTimeFormatter currentDateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        LocalDate currentDate = LocalDate.parse(fullDateString, currentDateFormatter);
        if (targetDate.getYear() != currentDate.getYear()) {
            appiumHelpers.waitForElementById(datePickerHeaderId).click();
            boolean found = false;
            int counter = 0;
            while (counter < 15) {
                List<WebElement> elementList = appiumDriver.findElementsByXPath(yearXpath);
                for (WebElement element : elementList) {
                    if (element.getAttribute("text")
                            .contentEquals(String.valueOf(targetDate.getYear()))) {
                        element.click();
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                } else {
                    appiumDriver.findElement(
                            MobileBy.AndroidUIAutomator(
                                    ("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
                                            + String.valueOf(targetDate.getYear())
                                            + "\").instance(0)).scrollToBeginning(1).setMaxSearchSwipes(0)")));
                }

                counter++;
            }
        }
        if (targetDate.getMonthValue() != currentDate.getMonthValue()) {
            int monthOffSet = targetDate.getMonthValue() - currentDate.getMonthValue();
            if (monthOffSet < 0) {
                monthArrow = "prev";
            } else {
                monthArrow = "next";
            }

            monthOffSet = Math.abs(monthOffSet);
            while (monthOffSet > 0) {
                appiumDriver.findElementById("android:id/" + monthArrow).click();
                monthOffSet = monthOffSet - 1;
            }
        }
        DateTimeFormatter targetDateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        String targetDateContentDescription = targetDateFormatter.format(targetDate);
        appiumDriver.findElementByAccessibilityId(targetDateContentDescription).click();
        appiumHelpers.waitForElementById(datePickerOkId).click();
    }

    /** If present, dismiss the overlay native os keyboard */
    @Override
    public void dismissKeyboard() {
        try {
            appiumDriver.hideKeyboard();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void selectElementFromPickerWheel(String pickerWheelOptionsXpath, String selectValue) {}

    /**
     * With a keypad overlay on the screen, press the key on the keypad.
     *
     * @param key The key to be pressed on the keypad.Refer to CustomKey enum.
     */
    @Override
    public void selectFromKeypad(CustomKeys key) {
        switch (key) {
            case RETURN:
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.ENTER));
                break;
            case TAB:
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.TAB));
                break;
            case SPACE:
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.SPACE));
                break;
            case SEARCH:
                ((AndroidDriver) appiumDriver).pressKey(new KeyEvent(AndroidKey.SEARCH));
                break;
            default:
                System.err.println("Incorrect key name: " + key);
        }
    }

    /** Provider return method used for binding this for Google Guice injection. */
    @Override
    public Actions get() {
        return this;
    }
}
