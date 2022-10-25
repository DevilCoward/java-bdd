package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import io.appium.java_client.MobileBy.ByIosNsPredicate;

public class ContainsTextIosPredicate extends ByIosNsPredicate {

    public ContainsTextIosPredicate(String textMatch) {
        super(
                String.format(
                        "label CONTAINS '%1$s' OR value CONTAINS '%1$s' OR name CONTAINS '%1$s'",
                        textMatch));
    }
}
