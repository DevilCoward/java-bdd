package com.luffy.testautomation.utils.helpers.locatorHelpers.Bys;

import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ContainsText;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ContainsTextIgnoreCase;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ContainsTextIgnoreCaseIosPredicate;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ContainsTextIosPredicate;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ExactText;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.strategies.ExactTextIosPredicate;
import org.openqa.selenium.By;

/**
 * Custom By Selectors. These static methods should be used instead of the various By strategy
 * classes in helpers.locatorHelpers.Bys.strategies
 *
 * <p>Usage Example:
 *
 * <p>import com.hsbc.digital.testautomation.utils.helpers.locatorHelpers.Bys.CustomBy;
 * actionHelpers.swipeToElement(CustomBy.containsText("someText"));
 */
public abstract class CustomBy {

    public static By containsText(String text) {
        return new ContainsText(text);
    }

    public static By containsTextIgnoreCase(String text) {
        return new ContainsTextIgnoreCase(text);
    }

    public static By containsTextIosPredicate(String text) {
        return new ContainsTextIosPredicate(text);
    }

    public static By containsTextIgnoreCaseIosPredicate(String text) {
        return new ContainsTextIgnoreCaseIosPredicate(text);
    }

    public static By exactText(String text) {
        return new ExactText(text);
    }

    public static By exactTextIosPredicate(String text) {
        return new ExactTextIosPredicate(text);
    }
}
