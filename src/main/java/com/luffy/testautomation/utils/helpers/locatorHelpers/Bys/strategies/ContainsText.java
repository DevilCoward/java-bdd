package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import org.openqa.selenium.By.ByXPath;

public class ContainsText extends ByXPath {

    public ContainsText(String textMatch) {
        super(
                String.format(
                        "//*[contains(@text, '%1$s') or contains(@label, '%1$s') or contains(@content-desc, '%1$s')]",
                        textMatch));
    }
}
