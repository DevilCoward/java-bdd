package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.contracts.instalments.InstalmentSampleAppLogin;
import com.luffy.testautomation.utils.common.Base;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import com.luffy.testautomation.utils.helpers.locatorHelpers.Bys.CustomBy;
import io.appium.java_client.AppiumDriver;
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

    private final String continueButtonId = "continueBtn";
    private final String launchInstallmentButtonId = "installmentsButton";
    private final String selectAccountId = "userList";
    protected final String accountListItemId = "android:id/text1";
    private final String activePlanListTestHarnessPageId = "activePlanList";
    protected final String historicalPlanListTestHarnessPageId = "historicalPlanSpinner";
    private final String entityId = "entityList";

    @FindBy(id = continueButtonId)
    public WebElement continueButton;

    @FindAll({@FindBy(id = accountListItemId)})
    public List<WebElement> accountList;

    @Inject
    public InstalmentSampleAppLoginScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public InstalmentSampleAppLoginScreen get() {
        if (System.getProperty("country").equalsIgnoreCase("malaysia")) {
            return new InstalmentSampleAppLoginScreenAndroid_malaysia(
                    appiumDriver, appiumHelpers, actionHelpers);
        }
        return this;
    }

    @Override
    public void selectEntity() {
        String entityName = System.getProperty("country");
        String localeName = System.getProperty("locale");
        appiumHelpers.waitForElementById(entityId).click();
        appiumHelpers.waitForElementById(accountListItemId);
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
        appiumHelpers.waitForElement(CustomBy.containsText(entityName)).click();
        actionHelpers.dismissKeyboard();
    }

    @Override
    public void clickContinueButton() {
        actionHelpers.swipeToElement(By.id(continueButtonId)).click();
    }

    @Override
    public void selectActivePlans(String planStatus) {
        // ios and android fields are different in the sample apps
        appiumHelpers.waitForElementById(activePlanListTestHarnessPageId).click();
        appiumHelpers.waitForElementById(accountListItemId);
        accountList.stream()
                .filter(webElement -> webElement.getText().contains(planStatus))
                .findFirst()
                .get()
                .click();
    }

    @Override
    public void clickLaunchInstalmentButtonOnSampleApp() {
        appiumHelpers.waitForElementById(launchInstallmentButtonId).click();
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForElementById(selectAccountId, timeout);
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElementById(selectAccountId);
    }
}
