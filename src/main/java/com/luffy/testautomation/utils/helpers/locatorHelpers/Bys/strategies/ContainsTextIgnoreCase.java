package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import org.openqa.selenium.By.ByXPath;

public class ContainsTextIgnoreCase extends ByXPath {

    public ContainsTextIgnoreCase(String text) {
        super(
                String.format(
                        "//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%1$s') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%1$s')]",
                        text.toLowerCase()));
    }
}
