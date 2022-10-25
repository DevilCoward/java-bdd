package com.luffy.testautomation.appautomation.contracts;

public interface VisibleInterface {

    Integer defaultTimeoutInSeconds = 30;

    boolean isVisible(Integer... timeoutInSeconds);

    void waitVisible();
}
