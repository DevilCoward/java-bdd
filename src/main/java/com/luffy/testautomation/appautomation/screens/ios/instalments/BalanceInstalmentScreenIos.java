package com.luffy.testautomation.appautomation.screens.ios.instalments;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.screens.common.Instalment.BalanceInstalmentScreen;
import com.luffy.testautomation.prjhelpers.LocaleCSVParser;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class BalanceInstalmentScreenIos extends BalanceInstalmentScreen
        implements Provider<BalanceInstalmentScreenIos> {

    private final String deleteIconId = "Delete";

    @Inject
    public BalanceInstalmentScreenIos(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers, actionHelpers);
        String staticText = "**/XCUIElementTypeStaticText[`label == '";
        amountEligibleForInstalmentSection = By.id("amountEligibleViewId");
        instalmentAmountEditBox = By.id("inputViewTextFieldId");
        closeButtonOnOfferCapBanner =
                MobileBy.iOSClassChain(
                        staticText
                                + LocaleCSVParser.getLocaleValue("close_cap_banner_text")
                                + "'`]");
        offerCapBanner = By.id("bannerview-message");
        inlineMessage = By.id("inlinestatus-text");
    }

    @Override
    public void inputInstalmentAmount(String instalmentAmount) {
        appiumHelpers.waitForElement(instalmentAmountEditBox).click();
        String inputAmount =
                appiumHelpers
                        .waitForElement(instalmentAmountEditBox)
                        .getText()
                        .replaceAll("[^\\d.]", "");
        int amountLength = inputAmount.length();
        while (amountLength > 0) {
            appiumHelpers.waitForElementById(deleteIconId).click();
            amountLength--;
        }
        appiumHelpers.waitForElement(instalmentAmountEditBox).sendKeys(instalmentAmount);
        appiumHelpers.waitForElement(amountEligibleForInstalmentSection).click();
    }

    @Override
    public BalanceInstalmentScreenIos get() {
        return this;
    }
}
