package com.luffy.testautomation.utils.helpers.actionHelpers;

import com.luffy.testautomation.utils.helpers.CustomKeys;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface ActionHelpers {
    String UP = "up";
    String DOWN = "down";
    String LEFT = "left";
    String RIGHT = "right";
    String MIDDLE = "middle";

    void swipeInDirection(String direction);

    void swipeInDirection(String direction, int edgeBuffer);

    WebElement swipeToElement(By selector, String direction, Boolean visible, Integer... optional);

    WebElement swipeToElement(By selector, String direction, Integer... optional);

    WebElement swipeToElement(By selector);

    boolean isElementOnPage(By selector, String direction, Boolean visible, Integer... optional);

    boolean isElementOnPage(By selector, String direction, Integer... optional);

    boolean isElementOnPage(By selector);

    void swipeByCoordinates(int startX, int startY, int endX, int endY);

    void swipeByPercentages(float startX, float startY, float endX, float endY);

    void swipeOnElement(WebElement swipeOn, String direction, String startPosition);

    void swipeOnElement(WebElement swipeOn, String direction);

    void clickAtCoordinates(int x, int y);

    void clickAtScreenPercent(float x, float y);

    void selectFromKeypad(char character);

    void selectFromKeypad(String sequence);

    void selectFromKeypad(CustomKeys key);

    void selectDateFromPicker(String date);

    void dismissKeyboard();

    void doubleTapOnElement(WebElement element);

    void selectElementFromPickerWheel(String pickerWheelOptionsXpath, String selectValue);
}
