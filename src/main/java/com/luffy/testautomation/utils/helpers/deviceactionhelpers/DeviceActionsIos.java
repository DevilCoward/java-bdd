package com.luffy.testautomation.utils.helpers.deviceactionhelpers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;

/**
 * Stores implementation of device actions for iOS <br>
 * Differences that occur between devices or OS version should be added using a new class and
 * returned using the provider <br>
 * Ex. DeviceActionsIos_iphone12Pro or DeviceActionsIos_OS13
 */
public class DeviceActionsIos extends DeviceActions implements Provider<DeviceActionHelpers> {

    @Inject
    public DeviceActionsIos(ActionHelpers actionHelpers) {
        super(actionHelpers);
    }

    @Override
    public DeviceActionHelpers get() {
        return this;
    }
}
