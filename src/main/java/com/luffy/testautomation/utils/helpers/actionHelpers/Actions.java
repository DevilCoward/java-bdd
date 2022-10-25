package com.luffy.testautomation.utils.helpers.actionHelpers;

import com.google.inject.Provider;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.CustomKeys;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public abstract class Actions implements ActionHelpers, Provider<ActionHelpers> {

    AppiumDriver appiumDriver;
    AppiumHelpers appiumHelpers;

    int defaultTries = 3;
    int defaultSwipeDelaySeconds = 3;

    int defaultEdgeBufferPercent = 30;

    int defaultSwipeDuration = 1;

    Actions(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
    }

    /**
     * Perform a swipe action in the specified direction: e.g. up, down, left, right. Note that this
     * is the swipe direction and not the resultant scroll direction that may occur. Uses the
     * default edge buffer percentage to determine the swipe distance.
     *
     * @param direction The direction of the swipe.
     */
    @Override
    public void swipeInDirection(String direction) {
        swipeInDirection(direction, defaultEdgeBufferPercent);
    }

    /**
     * Perform a swipe action in the specified direction: e.g. up, down, left, right. Note that this
     * is the swipe direction and not the resultant scroll direction that may occur. Uses the
     * specified edge buffer to determine swipe distance. edgeBuffer value is a percent of the
     * screen size and is applied to both edges of the screen.
     *
     * @param direction The direction of the swipe.
     * @param edgeBuffer The percent of the screen away from the edge to start and stop the swipe.
     */
    @Override
    public void swipeInDirection(String direction, int edgeBuffer) {
        int startX;
        int startY;
        int endX;
        int endY;

        Dimension size = appiumDriver.manage().window().getSize();

        switch (direction.toLowerCase()) {
            case UP:
                startX = endX = size.getWidth() / 2;
                endY = (int) (size.getHeight() * edgeBuffer / 100f);
                startY = (int) (size.getHeight() * (100 - edgeBuffer) / 100f);
                break;
            case DOWN:
                startX = endX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * edgeBuffer / 100f);
                endY = (int) (size.getHeight() * (100 - edgeBuffer) / 100f);
                break;
            case LEFT:
                startY = endY = size.getHeight() / 2;
                startX = (int) (size.getWidth() * (100 - edgeBuffer) / 100f);
                endX = (int) (size.getWidth() * edgeBuffer / 100f);
                break;
            case RIGHT:
                startY = endY = size.getHeight() / 2;
                startX = (int) (size.getWidth() * edgeBuffer / 100f);
                endX = (int) (size.getWidth() * (100 - edgeBuffer) / 100f);
                break;
            default:
                throw new NotImplementedException(
                        "Swipe Direction (" + direction + ") not implemented");
        }
        swipeByCoordinates(startX, startY, endX, endY);
    }

    /**
     * Swipe in a direction specified until a specific element is found visible or present (depends
     * on parameter visible) or until defaultTries are reached. Returns element if found and
     * visible. Note: direction is the swipe direction and not the potential scroll direction.
     * Optional parameter for max attempts and delay between swipes can be specified, otherwise
     * defaults are used.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     * @param direction the direction to swipe in to search for the element.
     * @param visible the found element is visible or simply present
     * @param optional takes potentially 3 additional integers, the first being the maximum number
     *     of tries (Default of 3), the second being the delay between swipes to search for the
     *     element (Default of 3 seconds) and the third being the percent of the screen away from
     *     the edge to start and stop the swipe (Default of 30 percent)
     */
    @Override
    public WebElement swipeToElement(
            By selector, String direction, Boolean visible, Integer... optional) {
        int retryTimes = optional.length > 0 ? optional[0] : defaultTries;
        int attemptsRemaining = retryTimes;
        int swipeDelay = optional.length > 1 ? optional[1] : defaultSwipeDelaySeconds;
        int edgeBufferPercent = optional.length > 2 ? optional[2] : defaultEdgeBufferPercent;
        while (attemptsRemaining-- > 0) {
            try {
                if (visible) {
                    return appiumHelpers.waitForElement(selector, swipeDelay);
                } else {
                    return appiumHelpers.waitForPresenceOfElementBy(selector, swipeDelay);
                }
            } catch (Exception ignored) {
            }

            if (attemptsRemaining > 0) {
                swipeInDirection(direction, edgeBufferPercent);
            }
        }
        throw new NoSuchElementException(
                String.format(
                        "Tried to swipe %s %s times to element with By selector: %s, but no such element was found %s",
                        direction, retryTimes, selector, (visible ? "visible" : "present")));
    }

    /**
     * Swipe in a direction specified until a specific element is found or until defaultTries are
     * reached. Returns element if found and visible. Note: direction is the swipe direction and not
     * the potential scroll direction. Optional parameter for max attempts and delay between swipes
     * can be specified, otherwise defaults are used.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     * @param direction the direction to swipe in to search for the element.
     * @param optional takes potentially 2 additional integers, the first being the maximum number
     *     of tries (Default of 3) and the second being the delay between swipes to search for the
     *     element (Default of 3 seconds).
     */
    @Override
    public WebElement swipeToElement(By selector, String direction, Integer... optional) {
        return swipeToElement(selector, direction, true, optional);
    }

    /**
     * Swipe up until specified element is found or until default 3 tries are reached. Returns
     * element if found and visible.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     */
    @Override
    public WebElement swipeToElement(By selector) {
        return swipeToElement(selector, UP);
    }

    /**
     * Swipe in a direction specified until a specific element is found visible or present (depends
     * on parameter visible). Note: direction is the swipe direction and not the potential scroll
     * direction. Optional parameter for max attempts and delay between swipes can be specified,
     * otherwise defaults are used.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     * @param direction the direction to swipe in to search for the element.
     * @param visible the found element is visible or simply present
     * @param optional takes potentially 2 additional integers, the first being the maximum number
     *     of tries (Default of 3) and the second being the delay between swipes to search for the
     *     element (Default of 3 seconds).
     */
    @Override
    public boolean isElementOnPage(
            By selector, String direction, Boolean visible, Integer... optional) {
        WebElement elementToFind = null;
        try {
            elementToFind = swipeToElement(selector, direction, visible, optional);
        } catch (Exception ignored) {
        }
        return elementToFind != null;
    }

    /**
     * Swipe in a direction specified until a specific element is found. Note: direction is the
     * swipe direction and not the potential scroll direction. Optional parameter for max attempts
     * and delay between swipes can be specified, otherwise defaults are used.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     * @param direction the direction to swipe in to search for the element.
     * @param optional takes potentially 2 additional integers, the first being the maximum number
     *     of tries (Default of 3) and the second being the delay between swipes to search for the
     *     element (Default of 3 seconds).
     */
    @Override
    public boolean isElementOnPage(By selector, String direction, Integer... optional) {
        return isElementOnPage(selector, direction, true, optional);
    }

    /**
     * Swipe up until specified element is found or until default 3 tries are reached.
     *
     * @param selector the org.openqa.selenium.By selector to be used to search for the element.
     */
    @Override
    public boolean isElementOnPage(By selector) {
        return isElementOnPage(selector, UP);
    }

    /**
     * Swipe from and to specified screen coordinates.
     *
     * @param startX The horizontal coordinate at which to start the swipe.
     * @param startY The vertical coordinate at which to start the swipe.
     * @param endX The horizontal coordinate at which to end the swipe.
     * @param endY The vertical coordinate at which to end the swipe.
     */
    @Override
    public void swipeByCoordinates(int startX, int startY, int endX, int endY) {

        new TouchAction(appiumDriver)
                .press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(defaultSwipeDuration)))
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
    }

    /**
     * Swipe from and to screen locations as a percentage of the screen size.
     *
     * @param startX the starting horizontal percentage of the screen.
     * @param startY the starting vertical percentage of the screen.
     * @param endX the ending horizontal percentage of the screen.
     * @param endY the ending vertical percentage of the screen.
     */
    @Override
    public void swipeByPercentages(float startX, float startY, float endX, float endY) {
        Dimension size = appiumDriver.manage().window().getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        swipeByCoordinates(
                (int) (width * startX / 100f),
                (int) (height * startY / 100f),
                (int) (width * endX / 100f),
                (int) (height * endY / 100f));
    }

    /**
     * Given a WebElement, swipe it in the specified direction.
     *
     * @param swipeOn the WebElement to be swiped.
     * @param direction the direction to swipe the element.
     * @param startPosition for full/half swipe from left to right -> startPosition should be left
     *     and the direction should be right for full/half swipe from right to left -> startPosition
     *     should be right and the direction should be left
     */
    @Override
    public void swipeOnElement(WebElement swipeOn, String direction, String startPosition) {
        int startX;
        int startY;
        int[] screenEdge = getScreenEdge(direction);

        switch (startPosition.toLowerCase()) {
            case LEFT:
                startX = swipeOn.getLocation().getX();
                startY = swipeOn.getLocation().getY();
                break;
            case MIDDLE:
                startX = (int) (swipeOn.getLocation().getX() + swipeOn.getSize().getWidth() * 0.5);
                startY = swipeOn.getLocation().getY();
                break;
            case RIGHT:
                startX = swipeOn.getLocation().getX() + swipeOn.getSize().getWidth() - 1;
                startY = swipeOn.getLocation().getY();
                break;
            default:
                throw new NotImplementedException("Invalid starting position: " + startPosition);
        }
        switch (direction.toLowerCase()) {
            case UP:
                swipeByCoordinates(startX, startY, startX, screenEdge[1]);
                break;
            case DOWN:
                swipeByCoordinates(startX, startY, startX, screenEdge[1]);
                break;
            case LEFT:
                swipeByCoordinates(startX, startY, screenEdge[0], startY);
                break;
            case RIGHT:
                swipeByCoordinates(startX, startY, screenEdge[0], startY);
                break;
            default:
                throw new RuntimeException("Invalid swipe direction: " + direction);
        }
    }

    /**
     * Given a WebElement, swipe it in the specified direction.
     *
     * @param swipeOn the WebElement to be swiped.
     * @param direction the direction to swipe the element.
     */
    @Override
    public void swipeOnElement(WebElement swipeOn, String direction) {
        swipeOnElement(swipeOn, direction, MIDDLE);
    }

    /**
     * Get the center point of the screen edge in the given screen direction.
     *
     * @param direction Which edge of the screen to find
     * @return Pair of Integer representing the x and y coordinates of the edge.
     */
    private int[] getScreenEdge(String direction) {
        Dimension size = appiumDriver.manage().window().getSize();
        int edgeX;
        int edgeY;
        int[] dimensions = new int[2];

        switch (direction.toLowerCase()) {
            case UP:
                edgeX = (int) (size.getWidth() * 0.5);
                edgeY = 0;
                break;
            case DOWN:
                edgeX = (int) (size.getWidth() * 0.5);
                edgeY = size.getHeight() - 1;
                break;
            case LEFT:
                edgeX = 0;
                edgeY = (int) (size.getHeight() * 0.5);
                break;
            case RIGHT:
                edgeX = size.getWidth() - 1;
                edgeY = (int) (size.getHeight() * 0.5);
                break;
            default:
                throw new NotImplementedException(
                        "Swipe Direction (" + direction + ") not implemented");
        }
        dimensions[0] = edgeX;
        dimensions[1] = edgeY;
        return dimensions;
    }

    /**
     * Click the specified x and y screen coordinates.
     *
     * @param x The horizontal coordinate to click.
     * @param y The vertical coordinate to click.
     */
    @Override
    public void clickAtCoordinates(int x, int y) {
        new TouchAction(appiumDriver).press(PointOption.point(x, y)).release().perform();
    }

    /**
     * Click the screen at a position determined by percentage of screen width and height Intended
     * to be a more screen size agnostic approach to click coordinates.
     *
     * @param x The percentage of the screen in the horizontal direction
     * @param y The percentage of the screen in the vertical direction
     */
    @Override
    public void clickAtScreenPercent(float x, float y) {
        Dimension size = appiumDriver.manage().window().getSize();
        clickAtCoordinates(
                (int) (size.getWidth() * (x / 100f)), (int) (size.getHeight() * (y / 100f)));
    }

    /**
     * Abstract method, overridden in platform specific classes.
     *
     * @param character The character to be selected
     */
    @Override
    public abstract void selectFromKeypad(char character);

    /**
     * Selects a sequence of characters from a keypad displayed on the screen.
     *
     * @param sequence String of characters to be pressed in order.
     */
    @Override
    public void selectFromKeypad(String sequence) {
        for (char character : sequence.toCharArray()) {
            selectFromKeypad(character);
        }
    }

    /**
     * Abstract method, overridden in platform specific classes.
     *
     * @param key The key to be selected from CustomKeys
     */
    @Override
    public abstract void selectFromKeypad(CustomKeys key);

    /**
     * Abstract method, overridden in platform specific classes. With a calendar overlay on the
     * screen, select the specified date from the calendar. Date format specified should be such
     * that it can be parsed by java.time.LocalDate.parse().
     *
     * @param date The date to be selected.
     */
    @Override
    public abstract void selectDateFromPicker(String date);

    /** If present, dismiss the overlay native os keyboard */
    @Override
    public abstract void dismissKeyboard();

    /**
     * Abstract method, overridden in platform specific classes. method is used to switch between
     * sample app to global app
     */
    @Override
    public void doubleTapOnElement(WebElement element) {
        TouchAction action = new TouchAction(appiumDriver);
        action.tap(
                TapOptions.tapOptions()
                        .withElement(ElementOption.element(element))
                        .withTapsCount(2));
        action.perform();
    }

    /** Provider return method used for binding this for Google Guice injection. */
    @Override
    public Actions get() {
        return this;
    }
}
