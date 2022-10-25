package com.luffy.testautomation.utils.helpers;

import com.google.inject.Inject;
import com.luffy.testautomation.utils.common.Base;
import io.appium.java_client.AppiumDriver;
import java.util.concurrent.TimeUnit;

public class MyTimer extends Base {

    final int SESSION_TIMEOUT_IN_MILLISECONDS = 600000;
    private final WebHelpers webHelpers;

    @Inject
    public MyTimer(AppiumDriver appiumDriver, WebHelpers webHelpers) {
        super(appiumDriver);
        this.webHelpers = webHelpers;
    }

    public long timeout() {
        long start_in_milliseconds =
                TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
        long end_in_milliseconds =
                TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS)
                        + SESSION_TIMEOUT_IN_MILLISECONDS;
        while (TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS)
                < end_in_milliseconds) {
            webHelpers.scrollScreen("down");
            webHelpers.scrollScreen("up");
        }
        return end_in_milliseconds - start_in_milliseconds;
    }
}
