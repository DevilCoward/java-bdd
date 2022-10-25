package com.luffy.testautomation.utils.helpers;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AndroidHelpers {
    private final AppiumDriver appiumDriver;
    private final AppiumHelpers appiumHelpers;
    private final ActionHelpers actionHelpers;

    @Inject
    public AndroidHelpers(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    /**
     * ********************************************************************************************
     * TAP Actions
     * ********************************************************************************************
     */
    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.clickAtCoordinates
     */
    @Deprecated
    public void tapOnScreen(int x, int y) {
        new AndroidTouchAction(appiumDriver).press(PointOption.point(x, y)).release().perform();
    }

    /**
     * ********************************************************************************************
     * SWIPE/SCROLL Actions
     * ********************************************************************************************
     */
    public void pullToRefresh() {
        Dimension size = appiumDriver.manage().window().getSize();
        int startx = size.getWidth() / 2;
        int starty;
        int endy;
        starty = (int) (size.getHeight() * 0.15);
        endy = (int) (size.getHeight() * 0.9);
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(startx, starty))
                .moveTo(PointOption.point(startx, endy))
                .waitAction(WaitOptions.waitOptions(ofSeconds(3)))
                .release()
                .perform();
    }

    public void pullToRefresh(By elementToPullFrom) {
        Dimension elementSize = appiumDriver.findElement(elementToPullFrom).getSize();
        Point elementLocation = appiumDriver.findElement(elementToPullFrom).getLocation();
        Dimension windowSize = appiumDriver.manage().window().getSize();
        int startX = ((elementSize.getWidth() / 2 + elementLocation.getX()));
        int startY = (int) (elementSize.getHeight() * .05 + elementLocation.getY());
        int endY = windowSize.getHeight();
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(startX, startY))
                .moveTo(PointOption.point(startX, endY))
                .waitAction(WaitOptions.waitOptions(ofSeconds(3)))
                .release()
                .perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public void swipeToElement(String selector) {
        this.appiumDriver.findElement(
                MobileBy.AndroidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\""
                                + selector
                                + "\").instance(0));"));
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public void swipeToElementById(String id) {
        String appPackage = appiumDriver.getCapabilities().getCapability("appPackage").toString();
        String resourceId = appPackage + ":id/" + id;
        appiumDriver.findElement(
                MobileBy.AndroidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\""
                                + resourceId
                                + "\").instance(0));"));
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public boolean swipeInDirectionAndIsElementDisplayed(
            String direction, int numOfTimes, By locatorStrategy) {
        boolean elementDisplayed = false;
        int count = 0;
        while (count <= numOfTimes) {
            try {
                try {
                    elementDisplayed = appiumDriver.findElement(locatorStrategy).isDisplayed();
                    return elementDisplayed;
                } catch (Exception e) {
                    swipeInDirection(direction, 1);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return elementDisplayed;
            }
        }
        return elementDisplayed;
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeInDirection or ActionHelpers.swipeToElement
     */
    @Deprecated
    public void swipeInDirection(String direction, int numOfTimes) throws IllegalArgumentException {
        Dimension size = appiumDriver.manage().window().getSize();
        int startx = size.getWidth() / 2;
        int starty;
        int endy;

        if (direction.equalsIgnoreCase("up")) {
            starty = (int) (size.getHeight() * 0.2);
            endy = (int) (size.getHeight() * 0.8);
        } else if (direction.equalsIgnoreCase("down")) {
            starty = (int) (size.getHeight() * 0.8);
            endy = (int) (size.getHeight() * 0.2);
        } else {
            throw new IllegalArgumentException("arg \"direction\" must be \"up\" or \"down\"");
        }

        TouchAction action = new TouchAction(appiumDriver);

        for (int i = 0; i < numOfTimes; i++) {
            action.press(PointOption.point(startx, starty))
                    .waitAction(WaitOptions.waitOptions(ofSeconds(1)))
                    .moveTo(PointOption.point(startx, endy))
                    .release()
                    .perform();
        }
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeByPercentages
     */
    @Deprecated
    public void swipeInDirectionForCalendar(String direction, int numOfTimes)
            throws IllegalArgumentException {
        Dimension size = appiumDriver.manage().window().getSize();
        int startx = size.getWidth() / 2;
        int starty;
        int endy;

        if (direction.equalsIgnoreCase("up")) {
            starty = (int) (size.getHeight() * 0.3);
            endy = (int) (size.getHeight() * 0.7);
        } else if (direction.equalsIgnoreCase("down")) {
            starty = (int) (size.getHeight() * 0.6);
            endy = (int) (size.getHeight() * 0.3);
        } else {
            throw new IllegalArgumentException("arg \"direction\" must be \"up\" or \"down\"");
        }

        TouchAction action = new TouchAction(appiumDriver);

        for (int i = 0; i < numOfTimes; i++) {
            action.press(PointOption.point(startx, starty))
                    .waitAction(WaitOptions.waitOptions(ofSeconds(1)))
                    .moveTo(PointOption.point(startx, endy))
                    .release()
                    .perform();
        }
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement(By.XPath) or
     * ActionHelpers.swipeToElement(By.Text
     */
    @Deprecated
    public void swipeToText(String selector) {
        this.appiumDriver.findElement(
                MobileBy.AndroidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"\\Q"
                                + selector
                                + "\\E\").instance(0))"));
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeByPercentages
     */
    @Deprecated
    public void verticalSwipeByPercentages(
            double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = appiumDriver.manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);

        new TouchAction(appiumDriver)
                .press(point(anchor, startPoint))
                .waitAction(waitOptions(ofSeconds(1)))
                .moveTo(point(anchor, endPoint))
                .release()
                .perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeOnElement
     */
    @Deprecated
    public void horizontalSwipe(WebElement webElement) {
        int x = webElement.getLocation().getX();
        int y = webElement.getLocation().getY();
        int x1 = (x * 20);
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(x1, y))
                .waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x, y))
                .release()
                .perform();
    }

    public void uiSelectorScrollIntoViewById(String idSelector, Integer... instancePos) {
        int instance = instancePos.length > 0 ? instancePos[0] : 0;
        appiumDriver.findElement(
                MobileBy.AndroidUIAutomator(
                        String.format(
                                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId(\"%s\").instance(%s))",
                                idSelector, instance)));
    }

    public void uiSelectorScrollIntoViewByClassWithText(String classnameSelector, String text) {
        appiumDriver.findElement(
                MobileBy.AndroidUIAutomator(
                        String.format(
                                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"%s\").className(\"%s\"))",
                                text, classnameSelector)));
    }

    /**
     * ********************************************************************************************
     * UTILITY Actions
     * ********************************************************************************************
     */
    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.dismissKeyboard
     */
    @Deprecated
    public void dismissKeyboardIfPresent() {
        try {
            appiumDriver.hideKeyboard();
        } catch (Exception ignored) {
        }
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.selectFromKeypad
     */
    @Deprecated
    public void selectNumberFromKeyPad(char number) {
        switch (number) {
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

            default:
                System.err.println("number is not configure " + number);
        }
    }

    public void switchToApp(String packageName, String activityName) {
        Activity activity = new Activity(packageName, activityName);
        activity.setStopApp(false);
        ((AndroidDriver<MobileElement>) appiumDriver).startActivity(activity);
    }

    /**
     * ********************************************************************************************
     * UiAutomator Selector Strategy for android native
     * ********************************************************************************************
     */
    public WebElement getAndroidUIAutomatorWebElementById(
            String locator, Integer... timeInSeconds) {

        MobileElement searchElement =
                (MobileElement)
                        appiumHelpers
                                .getWebDriverWaitForAGivenTime(timeInSeconds)
                                .until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                MobileBy.AndroidUIAutomator(
                                                        String.format(
                                                                "new UiSelector().resourceId(\"%s\")",
                                                                locator))));
        return searchElement;
    }

    public WebElement getAndroidUIAutomatorWebElementByClassName(
            String locator, Integer... timeInSeconds) {

        AndroidElement searchElement =
                (AndroidElement)
                        appiumHelpers
                                .getWebDriverWaitForAGivenTime(timeInSeconds)
                                .until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                MobileBy.AndroidUIAutomator(
                                                        String.format(
                                                                "new UiSelector().className(\"%s\")",
                                                                locator))));
        return searchElement;
    }

    public Boolean waitForAndroidUIAutomatorWebElementByClassNameToContains(
            String locator, String text, Integer... timeInSeconds) {
        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.textToBePresentInElementLocated(
                                MobileBy.AndroidUIAutomator(
                                        String.format(
                                                "new UiSelector().className(\"%s\")", locator)),
                                text));
    }

    public Boolean waitForAndroidUIAutomatorWebElementByIdToContains(
            String locator, String text, Integer... timeInSeconds) {

        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.textToBePresentInElementLocated(
                                MobileBy.AndroidUIAutomator(
                                        String.format(
                                                "new UiSelector().resourceId(\"%s\")", locator)),
                                text));
    }

    public WebElement waitForAndroidUISelectorByTextContains(
            String text, Integer... timeInSeconds) {

        AndroidElement searchElement =
                (AndroidElement)
                        appiumHelpers
                                .getWebDriverWaitForAGivenTime(timeInSeconds)
                                .until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                MobileBy.AndroidUIAutomator(
                                                        String.format(
                                                                "new UiSelector().textContains(\"%s\")",
                                                                text))));
        return searchElement;
    }

    public WebElement waitFotUiSelectorByClassNameToContainText(
            String selectorByClassName, String text, Integer... timeInSeconds) {

        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfElementLocated(
                                MobileBy.AndroidUIAutomator(
                                        String.format(
                                                "new UiSelector().textContains(\"%s\").className(\"%s\")",
                                                text, selectorByClassName))));
    }

    public WebElement getAnyUiSelectorsByInstance(
            String uiSelector, String securityAnswerConfirmationId, Integer... timeInSeconds) {
        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfElementLocated(
                                MobileBy.AndroidUIAutomator(
                                        String.format(uiSelector, securityAnswerConfirmationId))));
    }

    public void waitForInvisibilityOfAnyAndroidUIAutomatorSelector(
            String locator, Integer... timeInSeconds) {

        appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.invisibilityOfElementLocated(
                                MobileBy.AndroidUIAutomator(locator)));
    }

    public List<MobileElement> getElementsByAnyUiSelector(
            String uiSelector, Integer... timeInSeconds) {

        appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                MobileBy.AndroidUIAutomator(uiSelector)));
        return ((AndroidDriver<MobileElement>) appiumDriver)
                .findElementsByAndroidUIAutomator(uiSelector);
    }

    public WebElement waitForUiSelectorByTextByClassName(
            String selectorByClassName, String text, Integer... timeInSeconds) {

        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfElementLocated(
                                MobileBy.AndroidUIAutomator(
                                        String.format(
                                                "new UiSelector().text(\"%s\").className(\"%s\")",
                                                text, selectorByClassName))));
    }

    public WebElement waitForAndroidUISelectorByText(String text, Integer... timeInSeconds) {

        AndroidElement searchElement =
                (AndroidElement)
                        appiumHelpers
                                .getWebDriverWaitForAGivenTime(timeInSeconds)
                                .until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                MobileBy.AndroidUIAutomator(
                                                        String.format(
                                                                "new UiSelector().text(\"%s\")",
                                                                text))));
        return searchElement;
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.clickAtCoordinates
     */
    @Deprecated
    public void tapOnScreenByCoordinates(int x, int y) {
        new AndroidTouchAction(appiumDriver).press(PointOption.point(x, y)).release().perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.selectDateFromPicker
     */
    @Deprecated
    public void selectDateFromDatePicker(String date) {
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
                DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        LocalDate currentDate = LocalDate.parse(fullDateString, currentDateFormatter);
        if (targetDate.getYear() != currentDate.getYear()) {
            appiumHelpers.waitForElementById(datePickerHeaderId).click();
            boolean found = false;
            int counter = 0;
            while (counter < 10) {
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
                                            + targetDate.getYear()
                                            + "\").instance(0)).scrollToBeginning(1).setMaxSearchSwipes(0)")));
                    counter++;
                }
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
                DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        String targetDateContentDescription = targetDateFormatter.format(targetDate);
        appiumDriver.findElementByAccessibilityId(targetDateContentDescription).click();
        appiumHelpers.waitForElementById(datePickerOkId).click();
    }

    public void openDeviceSettings(String settingsAppActivityName) {
        final String settingsAppPackageName = "com.android.settings";
        final String settingsContentId = "android:id/content";
        // Switching to Settings app to reset the Location permissions for Device and App
        switchToApp(settingsAppPackageName, settingsAppActivityName);
        appiumHelpers.waitForElementById(settingsContentId);
    }

    public void openAppSettings(String appName, String appsText, String seeAllText) {
        final String appsLinkXpath =
                String.format("//android.widget.TextView[contains(@text,'%s')]", appsText);
        String appNameLinkXpath = String.format("//android.widget.TextView[@text='%s']", appName);
        final String seeAllXpath = String.format("//*[contains(@text,'%s')]", seeAllText);
        swipeInDirectionAndIsElementDisplayed("down", 1, By.xpath(appsLinkXpath));
        appiumDriver.findElementByXPath(appsLinkXpath).click();
        if (appiumHelpers.checkForElementByXpath(seeAllXpath, 3))
            appiumHelpers.findElementByXpath(seeAllXpath).click();
        actionHelpers.swipeToElement(By.xpath(appNameLinkXpath), "up", 6);
        appiumDriver.findElementByXPath(appNameLinkXpath).click();
    }
}
