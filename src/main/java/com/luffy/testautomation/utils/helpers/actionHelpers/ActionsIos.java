package com.luffy.testautomation.utils.helpers.actionHelpers;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.CustomKeys;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.CustomBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.remote.RemoteWebElement;

public class ActionsIos extends Actions {

    Integer defaultTimeoutInSeconds = 2;

    @Inject
    ActionsIos(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
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
        appiumHelpers
                .waitForElement(
                        CustomBy.exactTextIosPredicate(String.valueOf(character)),
                        defaultTimeoutInSeconds)
                .click();
    }

    /**
     * With a keypad overlay on the screen, press the key on the keypad.
     *
     * @param key The key to be pressed on the keypad.Refer to CustomKeys enum.
     */
    @Override
    public void selectFromKeypad(CustomKeys key) {
        appiumHelpers
                .waitForElement(
                        CustomBy.containsTextIgnoreCaseIosPredicate(key.toString()),
                        defaultTimeoutInSeconds)
                .click();
    }

    /**
     * With a calendar overlay on the screen, select the specified date from the calendar. Date
     * format specified should be such that it can be parsed by java.time.LocalDate.parse().
     *
     * @param date The date to be selected
     */
    @Override
    public void selectDateFromPicker(String date) {
        throw new NotImplementedException("Date Picker not yet implemented on iOS");
    }

    /**
     * If present, dismiss the overlay native os keyboard IOS differs from android implementation in
     * that either a click outside of the keyboard or pressing the submit button is required to
     * dismiss. Since pressing submit may not be the required behaviour, default will be to attempt
     * clicking outside
     */
    @Override
    public void dismissKeyboard() {
        try {
            ((IOSDriver) appiumDriver).hideKeyboard(HideKeyboardStrategy.TAP_OUTSIDE);
        } catch (Exception ignored) {
        }
    }

    /** Provider return method used for binding this for Google Guice injection. */
    @Override
    public Actions get() {
        return this;
    }

    @Override
    public void selectElementFromPickerWheel(String pickerWheelOptionsXpath, String selectValue) {
        appiumHelpers.waitForElementByXpath(pickerWheelOptionsXpath);
        while (!appiumHelpers
                .waitForElementByXpath(pickerWheelOptionsXpath)
                .getText()
                .contains(selectValue)) {
            Map<String, Object> params = new HashMap<>();
            params.put("order", "next");
            params.put("offset", 0.15);
            params.put(
                    "element",
                    ((RemoteWebElement)
                                    appiumHelpers.waitForElementByXpath(pickerWheelOptionsXpath))
                            .getId());
            appiumDriver.executeScript("mobile: selectPickerWheelValue", params);
        }
    }
}
