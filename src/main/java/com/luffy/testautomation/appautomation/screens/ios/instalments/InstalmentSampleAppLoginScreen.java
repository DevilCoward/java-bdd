package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.contracts.instalments.InstalmentSampleAppLogin;
import com.luffy.testautomation.prjhelpers.LocaleCSVParser;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InstalmentSampleAppLoginScreen extends Base
        implements InstalmentSampleAppLogin, Provider<InstalmentSampleAppLoginScreen> {

    protected AppiumDriver appiumDriver;
    protected AppiumHelpers appiumHelpers;
    protected ActionHelpers actionHelpers;

    private final String pickerClassName = "XCUIElementTypePickerWheel";
    private final String continueButtonId = "continueButton";
    private final String launchInstallmentsName =
            LocaleCSVParser.getLocaleValue("launch_instalments");
    private final String selectEntityId = "Select Entity";
    protected final String selectDefaultValue = "Default";
    protected final String selectPlanEmptyValue = "No_Active_Plan";
    protected final String selectPlanInvalidValue = "Invalid_Active_Plan";
    private final By secondaryAccountClassChain =
            MobileBy.iOSClassChain(
                    "**/XCUIElementTypePicker[`name == 'cardPicker'`]/XCUIElementTypePickerWheel");

    protected final By selectPlanClassChain =
            MobileBy.iOSClassChain(
                    "**/XCUIElementTypePicker[`name == 'activePlanPicker'`]/XCUIElementTypePickerWheel");

    @FindAll({@FindBy(className = pickerClassName)})
    public List<WebElement> picker;

    @FindBy(id = continueButtonId)
    public WebElement continueButton;

    @Inject
    public InstalmentSampleAppLoginScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver);
        this.appiumHelpers = appiumHelpers;
        this.appiumDriver = appiumDriver;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public InstalmentSampleAppLoginScreen get() {
        if (System.getProperty("country").equalsIgnoreCase("malaysia")) {
            return new InstalmentSampleAppLoginScreenIos_malaysia(
                    appiumDriver, appiumHelpers, actionHelpers);
        }
        return this;
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElementByClassName(pickerClassName);
    }

    @Override
    public void selectEntity() {
        String entityName = System.getProperty("country");
        String localeName = System.getProperty("locale");
        switch (entityName.toLowerCase()) {
            case "india":
                entityName = "India";
                break;
            case "malaysia":
                entityName = "Malaysia";
                break;
            case "singapore":
                entityName = "Singapore";
                break;
            case "hk":
                switch (localeName.toLowerCase()) {
                    case "en":
                        entityName = "HK";
                        break;
                    case "sc":
                        entityName = "HK(Chinese,Simplified)";
                        break;
                    case "tc":
                        entityName = "HK(Chinese,Traditional)";
                        break;
                    default:
                        throw new IllegalArgumentException("no this option!");
                }
                break;
            default:
                entityName = StringUtils.capitalize(entityName);
        }
        appiumHelpers.waitForElementById(selectEntityId);
        appiumDriver.findElementByClassName(pickerClassName).sendKeys(entityName);
        actionHelpers.dismissKeyboard();
    }

    @Override
    public void clickLaunchInstalmentButtonOnSampleApp() {
        appiumHelpers.waitForElementByName(launchInstallmentsName).click();
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementByClassName(pickerClassName, timeout);
    }

    @Override
    public void clickContinueButton() {
        continueButton.click();
    }

    @Override
    public void selectActivePlans(String activePlansType) {
        // ios and android fields are different in the sample apps
        if (activePlansType.contains("Plans Empty")) {
            appiumHelpers.waitForElement(secondaryAccountClassChain).sendKeys(selectPlanEmptyValue);
        } else if (activePlansType.contains("Plans Invalid")) {
            appiumHelpers
                    .waitForElement(secondaryAccountClassChain)
                    .sendKeys(selectPlanInvalidValue);
        } else {
            appiumHelpers.waitForElement(secondaryAccountClassChain).sendKeys(selectDefaultValue);
        }
    }
}
