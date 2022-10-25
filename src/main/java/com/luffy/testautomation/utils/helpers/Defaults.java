package com.luffy.testautomation.utils.helpers;

public enum Defaults {
    WAIT_TIME();

    private final int time;

    Defaults() {
        this.time = 40;
    }

    public int getTime() {
        return time;
    }
}
