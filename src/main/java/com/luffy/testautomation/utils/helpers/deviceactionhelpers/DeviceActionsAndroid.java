package com.luffy.testautomation.utils.helpers.deviceactionhelpers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;

/**
 * Stores implementation of device actions for Android <br>
 * Differences that occur between devices or OS version should be added using a new class and
 * returned using the provider <br>
 * Ex. DeviceActionsAndroid_samsungNote or DeviceActionsAndroid_OS10
 */
public class DeviceActionsAndroid extends DeviceActions implements Provider<DeviceActionHelpers> {

    @Inject
    public DeviceActionsAndroid(ActionHelpers actionHelpers) {
        super(actionHelpers);
    }

    @Override
    public void closeNotificationCentre() {
        // startY: 100 results in a out of bound error on Android
        // so use 99 instead
        actionHelpers.swipeByPercentages(0, 99f, 0, 10f);
    }

    @Override
    public DeviceActionHelpers get() {
        return this;
    }
}
