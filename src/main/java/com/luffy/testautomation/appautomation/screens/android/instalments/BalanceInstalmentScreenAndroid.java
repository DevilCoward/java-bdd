package com.luffy.testautomation.appautomation.screens.android.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.Instalment.BalanceInstalmentScreen;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class BalanceInstalmentScreenAndroid extends BalanceInstalmentScreen
        implements Provider<BalanceInstalmentScreenAndroid> {

    @Inject
    public BalanceInstalmentScreenAndroid(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        amountEligibleForInstalmentSection = By.id("balanceInstalmentOfferReviewItem");
        instalmentAmountEditBox = By.id("sInputFieldText");
        closeButtonOnOfferCapBanner = By.id("buttonOne");
        offerCapBanner = By.id("bannerDescription");
        inlineMessage = By.id("messageTextView");
    }

    @Override
    public void inputInstalmentAmount(String instalmentAmount) {
        appiumHelpers.waitForElement(instalmentAmountEditBox).click();
        appiumHelpers.waitForElement(instalmentAmountEditBox).clear();
        appiumHelpers.waitForElement(instalmentAmountEditBox).sendKeys(instalmentAmount);
        actionHelpers.dismissKeyboard();
    }

    @Override
    public BalanceInstalmentScreenAndroid get() {
        return this;
    }
}
