package com.luffy.testautomation.utils.helpers;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebElement;

public class WebHelpers {

    private final AppiumDriver appiumDriver;

    @Inject
    public WebHelpers(AppiumDriver appiumDriver, IosHelpers iosHelpers) {
        this.appiumDriver = appiumDriver;
    }

    public void scrollScreen(String move) {
        JavascriptExecutor js = this.appiumDriver;
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("direction", move);
        js.executeScript("mobile: scroll", scrollObject);
    }

    public void scrollUntilXPathElementFound(String move, String element) {
        RemoteWebElement parent = (RemoteWebElement) appiumDriver.findElement(By.xpath(element));
        String parentId = parent.getId();
        JavascriptExecutor js = this.appiumDriver;
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("direction", move);
        scrollObject.put("element", parentId);
        js.executeScript("mobile:scroll", scrollObject);
    }

    public void scrollToBottomOfTheScreen(String direction, int numberOfScrolls) {
        do {
            scrollScreen(direction);
            numberOfScrolls--;
        } while (numberOfScrolls > 0);
    }
}
