package com.luffy.testautomation.utils.helpers.deviceactionhelpers;

/**
 * Represents actions that you can perform on the device that are not tied to a specific
 * screen/modal. Examples of device actions: <br>
 * - open notification centre <br>
 * - rotate device orientation <br>
 * - toggle airplane mode (appium method not via UI)
 *
 * <p>Note: Device/System screens should have their own Page Objects that contains methods to
 * interact with those screens.
 *
 * <p>Example: Tapping on "hsbc payment" push notification via notification centre/panel screen.<br>
 * Steps example: <br>
 * 1) DeviceActionHelpers.openNotificationCentre() <br>
 * 2) NotificationCentre.tapOnHsbcPaymentPush() <br>
 */
public interface DeviceActionHelpers {

    /** Used to open the device's notification centre */
    void openNotificationCentre();

    /** Used to close the device's notification centre */
    void closeNotificationCentre();
}
