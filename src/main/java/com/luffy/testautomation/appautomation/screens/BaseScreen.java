package com.luffy.testautomation.appautomation.screens;

import com.luffy.testautomation.appautomation.contracts.VisibleInterface;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class BaseScreen extends Base implements VisibleInterface {
    private Logger log;

    @SuppressWarnings("rawtypes")
    protected AppiumDriver appiumDriver;

    protected AppiumHelpers appiumHelpers;

    protected By uniquePageLocator;

    @SuppressWarnings("rawtypes")
    public BaseScreen(AppiumDriver appiumDriver, AppiumHelpers appiumHelpers) {
        super(appiumDriver);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        log = LoggerFactory.getLogger(BaseScreen.class);
    }

    /**
     * Wait for the unique page identifier, return true/false if it appears or not. To be used when
     * a screen may or may not be expected, or if the current screen needs to be determined.
     *
     * @param timeoutInSeconds Optional, time in seconds to wait for the locator before returning
     * @return True if the element is present, otherwise false
     */
    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElement(uniquePageLocator, timeout);
    }

    /**
     * Implicitly wait for the unique page identifier to be present, if the element does not appear
     * within the implicit timeout, throw an exception. To be used in situations where page loading
     * needs to occur, and if it does not any further steps are invalid for the test.
     */
    @Override
    public void waitVisible() {
        appiumHelpers.waitForElement(uniquePageLocator);
    }

    /**
     * Return the entity specific version of this page. To be used by the provider method of each
     * page
     *
     * @param constructorArguments List of constructor arguments, in order for the entity PO
     * @return Instance of the page object for the current entity
     */
    public BaseScreen getEntityVersion(Object... constructorArguments) {
        Constructor<?> pageConstructor = getPageConstructorByParent(this);
        BaseScreen pageToUse;

        if (pageConstructor == null) {
            pageToUse = this;
        } else {
            try {
                pageToUse = (BaseScreen) pageConstructor.newInstance(constructorArguments);
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | RuntimeException e) {
                pageToUse = this;
            }
        }

        pageToUse.log = LoggerFactory.getLogger(pageToUse.getClass());
        pageToUse.log.info(String.format("Using %s", pageToUse.getClass().getName()));
        return pageToUse;
    }

    /**
     * Given a base class, return the list of possible names for its entity or locale specific
     * versions
     *
     * @param base The base class to get alternate names for
     * @param country The country to get the alternate class for
     * @return Array of strings representing possible class names
     */
    private static String[] getPageClassNames(String base, String country, String locale) {
        return new String[] {
            String.format("%s_%s", base, country), String.format("%s_%s_%s", base, country, locale)
        };
    }

    /**
     * Get the constructor for the entity specific page object version of a given page parentClass
     * param should generally by the Platform specific page
     *
     * @param parentClass The base class to get the entity specific PO for
     * @return Constructor for the PO
     */
    private static Constructor<?> getPageConstructorByParent(BaseScreen parentClass) {
        String parentClassName = parentClass.getClass().getName();

        Constructor<?>[] constructors;
        for (String classAttempt :
                getPageClassNames(
                        parentClassName,
                        System.getProperty("country"),
                        System.getProperty("locale"))) {
            try {
                constructors = Class.forName(classAttempt).getDeclaredConstructors();
            } catch (ClassNotFoundException exception) {
                continue;
            }
            return constructors[0];
        }
        return null;
    }
}
