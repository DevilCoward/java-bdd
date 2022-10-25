package com.luffy.testautomation.utils.helpers.deviceactionhelpers;

import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;

/** Stores implementation of device actions supported by both Android and iOS devices. */
public abstract class DeviceActions implements DeviceActionHelpers {

    ActionHelpers actionHelpers;

    public DeviceActions(ActionHelpers actionHelpers) {
        this.actionHelpers = actionHelpers;
    }

    @Override
    public void openNotificationCentre() {
        actionHelpers.swipeByPercentages(0, 0, 0, 90f);
    }

    @Override
    public void closeNotificationCentre() {
        actionHelpers.swipeByPercentages(0, 100f, 0, 10f);
    }
}
