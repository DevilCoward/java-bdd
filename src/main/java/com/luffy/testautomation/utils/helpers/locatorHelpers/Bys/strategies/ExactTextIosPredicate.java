package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import io.appium.java_client.MobileBy.ByIosNsPredicate;

public class ExactTextIosPredicate extends ByIosNsPredicate {

    public ExactTextIosPredicate(String textMatch) {
        super(String.format("label == '%1$s' OR value == '%1$s' OR name == '%1$s'", textMatch));
    }
}
