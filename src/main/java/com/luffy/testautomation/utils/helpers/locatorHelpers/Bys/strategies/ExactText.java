package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies;

import org.openqa.selenium.By.ByXPath;

public class ExactText extends ByXPath {
    public ExactText(String textMatch) {
        super(
                String.format(
                        "//*[@text='%1$s' or @label='%1$s' or @content-desc='%1$s']", textMatch));
    }
}
