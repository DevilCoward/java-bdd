package com.luffy.testautomation.utils.helpers;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IosHelpers {

    private final AppiumDriver appiumDriver;
    private final AppiumHelpers appiumHelpers;

    @Inject
    public IosHelpers(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
    }

    /**
     * ********************************************************************************************
     * FIND helper methods. Finds an element Suggested use: 1) For finding static screen elements
     * ********************************************************************************************
     */
    // IOS Predicate can be used in most cases to replace XPath.
    // Predicates provides a native approach to locating element which results in much faster look
    // up time.
    // Conversion Example:
    // XPath: //XCUIElementTypeStaticText[contains(@label,"Button text")]
    // Predicate String : "type == 'XCUIElementTypeStaticText' AND label CONTAINS 'Button text'"

    /**
     * Used to find an element using iOS NS Predicate.
     *
     * @param predicateString Example: "type == 'ElementType' AND label == 'text' AND visible == 1"
     * @return WebElement
     */
    public WebElement findElementByIOSPredicate(String predicateString) {
        return this.appiumDriver.findElement(MobileBy.iOSNsPredicateString(predicateString));
    }

    /**
     * Used to find elements using iOS NS Predicate.
     *
     * @param predicateString Example: "type == 'ElementType' AND label == 'text' AND visible == 1"
     * @return a list of WebElement
     */
    public List<WebElement> findElementsByIOSPredicate(String predicateString) {
        return this.appiumDriver.findElements(MobileBy.iOSNsPredicateString(predicateString));
    }

    public WebElement findElementByText(String text) {
        return findElementByIOSPredicate(
                String.format("label == '%1$s' OR value == '%1$s' OR name == '%1$s'", text));
    }

    public WebElement findElementByPartialText(String text) {
        return findElementByIOSPredicate(
                String.format(
                        "label CONTAINS '%1$s' OR value CONTAINS '%1$s' OR name CONTAINS '%1$s'",
                        text));
    }

    /**
     * ********************************************************************************************
     * WAIT helper methods. Waits for the visibility of an element. Suggested use: 1) For dynamic
     * screen elements 2) When transitioning between screens
     * ********************************************************************************************
     */
    public WebElement waitForElementByIOSPredicate(
            String predicateString, Integer... timeInSeconds) {
        WebDriverWait wait = appiumHelpers.getWebDriverWaitForAGivenTime(timeInSeconds);

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        MobileBy.iOSNsPredicateString(predicateString)));
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
        new IOSTouchAction(appiumDriver).press(PointOption.point(x, y)).release().perform();
    }

    public void doubleTap(String id) {
        JavascriptExecutor js = this.appiumDriver;
        HashMap map = new HashMap();
        map.put("element", id);
        js.executeScript("mobile: doubleTap", map);
    }

    /**
     * ********************************************************************************************
     * SWIPE/SCROLL Actions
     * ********************************************************************************************
     */
    public void pullToRefresh() {
        Dimension size = appiumDriver.manage().window().getSize();
        int endPoint = (int) (size.height * .9);
        int startPoint = (int) (size.height * .5);
        int anchor = size.width / 2;
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(anchor, endPoint))
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
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .release()
                .perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeByCoordinates
     */
    @Deprecated
    public void swipeCoordinates(int[] startCoordinates, int[] endCoordinates) {
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(startCoordinates[0], startCoordinates[1]))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(endCoordinates[0], endCoordinates[1]))
                .release()
                .perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeInDirection
     */
    @Deprecated
    public void swipeToElement(String move) {
        JavascriptExecutor js = this.appiumDriver;
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("direction", move);
        js.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public void swipeToText(String selector, int maxSwipes) {
        /*
            Why not find the element once and just call element.isDisplayed in the loop?
            Once an element has been located, isDisplayed will always return the same value
            You need to "relocate" an element to see if its visibility has changed

            We use a lambda expression (a "BooleanSupplier") for this
        */
        BooleanSupplier isDisplayed = () -> appiumHelpers.findElementByText(selector).isDisplayed();

        int loop = 0;

        while (!isDisplayed.getAsBoolean() && loop < maxSwipes) {
            swipeUpWithCoordinates(.4, .6);
            loop++;
        }
        if (!isDisplayed.getAsBoolean()) {
            throw new NoSuchElementException("Couldn't swipe to text: " + selector);
        }
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeByCoordinates
     */
    @Deprecated
    public void swipeUpWithCoordinates(double endPointPercent, double startPointPercent) {
        Dimension size = appiumDriver.manage().window().getSize();
        int endPoint = (int) (size.height * endPointPercent);
        int startPoint = (int) (size.height * startPointPercent);
        int anchor = size.width / 2;
        TouchAction action = new TouchAction(appiumDriver);
        action.press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(anchor, endPoint))
                .release()
                .perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeInDirection
     */
    @Deprecated
    public void swipeInDirectionWhenKeypadDisplayed(String oppositeDirection) {
        JavascriptExecutor js = this.appiumDriver;
        HashMap scrollObject = new HashMap();
        scrollObject.put("direction", oppositeDirection);
        js.executeScript("mobile: swipe", scrollObject);
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeInDirection
     */
    @Deprecated
    public void horizontalSwipe(WebElement webElement) {
        int x = webElement.getLocation().getX();
        int y = webElement.getLocation().getY();
        int w = webElement.getSize().getWidth();
        TouchAction actions = new TouchAction(appiumDriver);
        actions.longPress(PointOption.point(w, y))
                .waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x, y))
                .release()
                .perform();
    }

    public void scrollObjectByTextAndDirection(String objectText, String direction) {
        HashMap scrollObject = new HashMap<>();
        JavascriptExecutor jse = this.appiumDriver;
        scrollObject.put("predicateString", "value == '" + objectText + "'");
        scrollObject.put("direction", direction);
        jse.executeScript("mobile: scroll", scrollObject);
    }

    /**
     * ********************************************************************************************
     * UTILITY Actions
     * ********************************************************************************************
     */
    public void switchToApp(String id) {
        JavascriptExecutor js = this.appiumDriver;
        HashMap map = new HashMap();
        map.put("bundleId", id);
        js.executeScript("mobile: launchApp", map);
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.selectFromKeypad
     */
    @Deprecated
    public void selectNumbersFromKeyPad(String numbersToInput) {
        for (int x = 0; x < numbersToInput.length(); x++) {
            String number = "" + numbersToInput.charAt(x);
            appiumDriver.findElementByName(number).click();
        }
    }

    /**
     * ********************************************************************************************
     * Class Chain Selector. Alternative to Xpath faster and better
     * ********************************************************************************************
     */
    public WebElement waitIosByClassChain(String iosClassChainString, Integer... timeInSeconds) {
        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfElementLocated(
                                MobileBy.iOSClassChain(iosClassChainString)));
    }

    public boolean waitForInvisibilityOfIosByClassChain(
            String iosClassChainString, Integer... timeInSeconds) {
        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.invisibilityOfElementLocated(
                                MobileBy.iOSClassChain(iosClassChainString)));
    }

    public boolean isIosByClassChainDisplayed(
            String iosClassChainString, Integer... timeInSeconds) {
        try {
            return appiumHelpers
                    .getWebDriverWaitForAGivenTime(timeInSeconds)
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    MobileBy.iOSClassChain(iosClassChainString)))
                    .isDisplayed();
        } catch (Exception ignore) {
            return false;
        }
    }

    public List<WebElement> waitAllIosByClassChain(
            String iosClassChainString, Integer... timeInSeconds) {
        return appiumHelpers
                .getWebDriverWaitForAGivenTime(timeInSeconds)
                .until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                MobileBy.iOSClassChain(iosClassChainString)));
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public void scrollToAccount(String accountNumber, List<WebElement> accountList) {
        int count = 0;
        while (accountList.stream()
                        .noneMatch(
                                webElement ->
                                        webElement.getText().contains(accountNumber)
                                                && webElement.isDisplayed())
                && count < 10) {
            swipeInDirectionWhenKeypadDisplayed("up");
            count++;
        }
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.clickAtCoordinates
     */
    @Deprecated
    public void tapOnScreenByCoordinates(int x, int y) {
        new IOSTouchAction(appiumDriver).press(PointOption.point(x, y)).release().perform();
    }

    /**
     * IOT reduce maintenance, and streamline method usage, this method is deprecated in favour of
     * the platform agnostic ActionHelpers.swipeToElement
     */
    @Deprecated
    public void swipeIfElementNotDisplayed(String locatorType, String locator, String direction) {
        switch (locatorType) {
            case "id":
                if (!appiumHelpers.checkForElementById(locator)) {
                    swipeInDirectionWhenKeypadDisplayed(direction);
                }
                break;

            case "name":
                if (!appiumHelpers.checkForElementByName(locator)) {
                    swipeInDirectionWhenKeypadDisplayed(direction);
                }
                break;

            case "xpath":
                if (!appiumHelpers.checkForElementByXpath(locator)) {
                    swipeInDirectionWhenKeypadDisplayed(direction);
                }
                break;
        }
    }
}
