package com.luffy.testautomation.utils.helpers;

import static java.lang.String.format;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

import com.google.inject.Inject;
import com.luffy.testautomation.appautomation.config.MyIosModule;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.core.api.Scenario;
import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppiumHelpers {

    private static final Logger log = LoggerFactory.getLogger(AppiumHelpers.class);
    private final AppiumDriver appiumDriver;
    private WebDriverWait webDriverWait;
    Config config;

    @Inject
    public AppiumHelpers(AppiumDriver appiumDriver, Config config) {
        this.appiumDriver = appiumDriver;
        this.config = config;
        this.webDriverWait =
                (WebDriverWait)
                        new WebDriverWait(appiumDriver, 20, 10)
                                .ignoring(NoSuchElementException.class);
    }

    /**
     * ********************************************************************************************
     * FIND helper methods. Finds an element Suggested use: 1) For finding static screen elements
     * ********************************************************************************************
     */
    public WebElement elementWithText(By criteria, String text) {
        log.info(format("Looking for the element by %s with text %s", criteria, text));
        return webDriverWait.until(
                appiumDriver ->
                        appiumDriver.findElements(criteria).stream()
                                .filter(e -> e.getText().contains(text))
                                .findFirst()
                                .orElseThrow(
                                        () ->
                                                new NoSuchElementException(
                                                        format(
                                                                "Element not found with text '%s'",
                                                                text))));
    }

    public WebElement elementWithTextIos(By criteria, String text) {
        log.info(format("Looking for element by %s with text %s", criteria, text));
        return webDriverWait.until(
                appiumDriver ->
                        appiumDriver.findElements(criteria).stream()
                                .findFirst()
                                .orElseThrow(
                                        () ->
                                                new NoSuchElementException(
                                                        format(
                                                                "Element not found with text '%s'",
                                                                text))));
    }

    public WebElement findElementByXpath(String xpathOfElement) {
        return this.appiumDriver.findElementByXPath(xpathOfElement);
    }

    public WebElement findElementByText(String text) {
        String xPath = String.format("//*[@text='%1$s' or @label='%1$s']", text);
        return appiumDriver.findElementByXPath(xPath);
    }

    public WebElement findElementByPartialText(String text) {
        String xPath =
                String.format("//*[contains(@text, '%1$s') or contains(@label, '%1$s')]", text);
        return appiumDriver.findElementByXPath(xPath);
    }

    /**
     * ********************************************************************************************
     * WAIT helper methods. Waits for the visibility of an element. Suggested use: 1) For dynamic
     * screen elements 2) When transitioning between screens
     * ********************************************************************************************
     */
    WebDriverWait getWebDriverWaitForAGivenTime(Integer[] timeInSeconds) {
        Integer waitTimeInSecond =
                timeInSeconds.length > 0 ? timeInSeconds[0] : Defaults.WAIT_TIME.getTime();
        return (WebDriverWait)
                new WebDriverWait(this.appiumDriver, waitTimeInSecond, 10)
                        .ignoring(NoSuchElementException.class)
                        .ignoring(WebDriverException.class)
                        .ignoring(UnreachableBrowserException.class)
                        .ignoring(ProtocolException.class);
    }

    private void fluentWait(WebElement locator, int seconds) {
        FluentWait wait =
                new WebDriverWait(appiumDriver, (long) seconds)
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class);
        try {
            wait.until(ExpectedConditions.visibilityOf(locator));
        } catch (NoSuchElementException | StaleElementReferenceException var) {
            log.error("Element not found", var);
            throw var;
        }
    }

    public void waitForElementFluently(WebElement locator, int seconds) {
        fluentWait(locator, seconds);
    }

    public WebElement visibleElement(By by) {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement waitForElementById(String mobileElementId, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(mobileElementId)));
    }

    public List<WebElement> waitForListOfElementByClass(
            List<WebElement> mobileElementClassName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(
                ExpectedConditions.visibilityOfAllElements(mobileElementClassName));
    }

    public WebElement waitForElementByAccessibilityId(
            String accessibilityId, Integer... timeInSeconds) {
        appiumDriver.context(nativeContext());
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        MobileBy.AccessibilityId(accessibilityId)));
    }

    public WebElement waitForElementByClassName(
            String mobileElementClassName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className(mobileElementClassName)));
    }

    public WebElement waitForElementByXpath(String mobileElementXpath, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(mobileElementXpath)));
    }

    public WebElement waitForElementTobeClickable(By bySelector, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(ExpectedConditions.elementToBeClickable(bySelector));
    }

    public WebElement waitForElementTobeClickableById(
            String mobileElementId, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(
                ExpectedConditions.elementToBeClickable(By.id(mobileElementId)));
    }

    public WebElement waitForElementTobeClickableByName(
            String mobileElementName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(
                ExpectedConditions.elementToBeClickable(By.name(mobileElementName)));
    }

    public WebElement waitForElementByCssSelector(
            String mobileElementCss, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(mobileElementCss)));
    }

    public WebElement waitForElementByName(String mobileElementName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name(mobileElementName)));
    }

    public List<WebElement> waitForAllElements(
            List<WebElement> mobileElements, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(ExpectedConditions.visibilityOfAllElements(mobileElements));
    }

    public boolean waitForTextPresentInElement(
            WebElement element, String expectedText, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.textToBePresentInElement(element, expectedText));
    }

    public boolean waitForMatchPartialTextPresentInElement(
            WebElement webElement, String matchExpectedText, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        String attribute;
        if (System.getProperty("platform").equalsIgnoreCase("android")) {
            attribute = "text";
        } else {
            attribute = "label";
        }
        return webDriverWait.until(
                ExpectedConditions.attributeContains(webElement, attribute, matchExpectedText));
    }

    public WebElement waitForVisibilityElementTobeByClassName(
            String mobileElementClassName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className(mobileElementClassName)));
    }

    public WebElement waitForVisibilityElementToBeByCssSelector(
            String mobileElementCss, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(mobileElementCss)));
    }

    public WebElement waitForVisibilityElementTobeById(
            String mobileElementId, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(mobileElementId)));
    }

    public WebElement waitForVisibilityElementTobeByName(
            String mobileElementName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name(mobileElementName)));
    }

    public WebElement waitForPresenceOfElementBy(By mobileElement, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return this.webDriverWait.until(ExpectedConditions.presenceOfElementLocated(mobileElement));
    }

    public void waitForSeconds(int waitingSecond) {
        try {
            Thread.sleep(waitingSecond * 1000);
        } catch (InterruptedException e) {
            log.info(getStackTrace(e));
        }
    }

    /**
     * ********************************************************************************************
     * WAIT FOR INVISIBILITY helper methods. Waits for an element to be invisible (to no longer be
     * displayed) Suggested use: 1) Wait for element to disappear before proceeding to next step
     * ********************************************************************************************
     */
    public void waitForInvisibilityElementTobeById(
            String mobileElementId, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        webDriverWait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.id(mobileElementId)));
    }

    public void waitForInvisibilityElementTobeByClassName(
            String mobileElementClassName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        webDriverWait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        By.className(mobileElementClassName)));
    }

    public void waitForInvisibilityElementTobeByName(
            String mobileElementName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        webDriverWait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.name(mobileElementName)));
    }

    public void waitForInvisibilityElementToBeByCssSelector(
            String mobileElementCss, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        webDriverWait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(mobileElementCss)));
    }

    public void waitForInvisibilityElementToBeByXpath(
            String mobileElementXPath, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        this.webDriverWait.until(
                ExpectedConditions.invisibilityOf(
                        this.appiumDriver.findElement(By.xpath(mobileElementXPath))));
    }
    /**
     * *******************************************************************************************
     * CHECK VISIBILITY helper methods. Check to see if an element is visible and returns
     * true/false. Suggested use: 1) If the next action is different depending on if element is
     * visible
     * *******************************************************************************************
     */
    public boolean checkForElementById(String mobileElementId, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id(mobileElementId)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByName(String mobileElementName, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.name(mobileElementName)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByXpath(String mobileElementXpath, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath(mobileElementXpath)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByClassName(
            String mobileElementClassName, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        try {
            webDriverWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.className(mobileElementClassName)));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByAttributeValue(
            WebElement mobileElement, String attribute, String value, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait.until(
                    ExpectedConditions.attributeToBe(mobileElement, attribute, value));
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByCssSelector(String mobileElementCss, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.cssSelector(mobileElementCss)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByPartialText(String text, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(text)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public WebElement waitForElement(By selector, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(selector));
    }

    public boolean isClickable(WebElement webElement, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isVisible(WebElement webElement, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public Boolean checkForPresenceOfElementsBy(By mobileElement, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return !this.webDriverWait
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(mobileElement))
                    .isEmpty();
        } catch (Exception ignored) {
            return false;
        }
    }

    public Boolean checkForVisibilityOfElementsBy(By mobileElement, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return !this.webDriverWait
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(mobileElement))
                    .isEmpty();
        } catch (Exception ignored) {
            return false;
        }
    }

    public Boolean checkForPartialText(
            WebElement webElement, String matchExpectedText, Integer... timeInSeconds) {
        try {
            waitForMatchPartialTextPresentInElement(webElement, matchExpectedText, timeInSeconds);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForElementByAccessibilityId(
            String mobileElementId, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    MobileBy.AccessibilityId(mobileElementId)))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * ********************************************************************************************
     * ACTION helper methods Used to trigger an action on screen
     * ********************************************************************************************
     */
    public void clickOnOSBackButton() {
        String strBackCommand = "adb shell input keyevent 4";
        Runtime run = Runtime.getRuntime();
        try {
            Thread.sleep(2000);
            run.exec(strBackCommand);
        } catch (InterruptedException | IOException e) {
            log.info(getStackTrace(e));
        }
    }

    /**
     * ********************************************************************************************
     * Click and hold on a given element for given seconds
     *
     * @param element WebElement to click and hold on
     * @param durationInSeconds duration, in seconds, to hold
     *     ****************************************************************************************
     */
    public void longPress(WebElement element, int durationInSeconds) {
        Rectangle rectangle = element.getRect();
        int x = rectangle.getX() + rectangle.getWidth() / 2;
        int y = rectangle.getY() + rectangle.getHeight() / 2;

        TouchAction action = new TouchAction(appiumDriver);
        action.longPress(PointOption.point(x, y))
                .waitAction(
                        new WaitOptions().withDuration(Duration.ofMillis(durationInSeconds * 1000)))
                .release()
                .perform();
    }

    public void navigateBack(int noOfStepsBackward) {
        IntStream.rangeClosed(1, noOfStepsBackward).forEach(e -> appiumDriver.navigate().back());
    }

    /**
     * ********************************************************************************************
     * Utility helper methods. Various helper methods to trigger Appium actions
     * ********************************************************************************************
     */
    public File captureScreenShot(Scenario scenario) {
        String pattern = "yyyy-MM-dd_HH.mm.ss";
        File srcFile = appiumDriver.getScreenshotAs(OutputType.FILE);
        SimpleDateFormat date = new SimpleDateFormat(pattern);
        String filename = date.format(new Date());
        File targetFile =
                new File(
                        "build/reports/tests/cucumber-extent/screenshots"
                                + "/"
                                + filename
                                + scenario.getName().replace('/', '_')
                                + ".png");
        try {
            FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return targetFile;
    }

    /**
     * ********************************************************************************************
     * This method converts captured screenshot into base64 image format
     *
     * @param scenario parameter to capture screen shot
     * @return -Base64 image
     *     ********************************************************************************************
     */
    public String getBase64Screenshot(Scenario scenario) {
        return getBase64Screenshot(captureScreenShot(scenario));
    }

    public String getBase64Screenshot(File screenshotFile) {
        String base64Screenshot = "";
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
            base64Screenshot =
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return base64Screenshot;
    }

    public String testCapture(String regularExpression, String input) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(input);
        String result = null;
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public String getPageSource() {
        return this.appiumDriver.getPageSource();
    }

    public String nativeContext() {
        return this.setContextToIndex(0);
    }

    private String setContextToIndex(int index) {
        Set<String> handles = appiumDriver.getContextHandles();
        List<String> handleList = new ArrayList<>(handles);
        return handleList.get(index);
    }

    public int[] getCenterCoordinates(WebElement element) {
        int xCoordinate = element.getLocation().getX();
        int yCoordinate = element.getLocation().getY();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        int xMiddleOfElement = xCoordinate + (width / 2);
        int yMiddleOfElement = yCoordinate + (height / 2);

        return new int[] {xMiddleOfElement, yMiddleOfElement};
    }

    public int[] getCoordinates(WebElement element) {
        int xCoordinate = element.getLocation().getX();
        int yCoordinate = element.getLocation().getY();
        return new int[] {xCoordinate, yCoordinate};
    }

    public boolean isValidDateFormat(String dateToValidate, String expectedDatePattern) {
        LocalDate localDate;
        try {
            DateTimeFormatter formatter =
                    new DateTimeFormatterBuilder()
                            .appendPattern(expectedDatePattern)
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                            .toFormatter();
            localDate = LocalDate.parse(dateToValidate, formatter);
            String result = localDate.format(formatter);
            return result.equals(dateToValidate);
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForInvisibility(By selector, Integer... timeInSeconds) {
        this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
    }

    public boolean checkForElement(By selector, Integer... timeInSeconds) {
        try {
            this.webDriverWait = getWebDriverWaitForAGivenTime(timeInSeconds);
            return webDriverWait
                    .until(ExpectedConditions.visibilityOfElementLocated(selector))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean checkForInvisibility(By selector, Integer... timeInSeconds) {
        try {
            waitForInvisibility(selector, timeInSeconds);
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    // This method helps to switch back to iOS app from other app opened from it
    public void switchApp() {
        /**
         * Added this thread sleep because it will wait until switch to third-party app completely.
         * Otherwise, the appiumDriver.activateApp method will not effect.
         */
        waitForSeconds(2);
        if (Boolean.parseBoolean(System.getProperty("browserstack"))) {
            if (config.hasPath("bundleId")) {
                appiumDriver.activateApp(config.getString("bundleId"));
            }
        } else {
            appiumDriver.activateApp(MyIosModule.bundleId);
        }
    }
}
