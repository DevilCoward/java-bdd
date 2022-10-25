package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import io.appium.java_client.MobileBy.ByIosNsPredicate;

public class ContainsTextIgnoreCaseIosPredicate extends ByIosNsPredicate {

    public ContainsTextIgnoreCaseIosPredicate(String textMatch) {
        super(
                String.format(
                        "label CONTAINS[c] '%1$s' OR value CONTAINS[c] '%1$s' OR name CONTAINS[c] '%1$s'",
                        textMatch));
    }
}
