package com.luffy.testautomation.appautomation.screens.common.Instalment;

import com.luffy.testautomation.appautomation.contracts.instalments.BalanceInstalment;
import com.luffy.testautomation.appautomation.screens.BaseScreen;
import com.luffy.testautomation.prjhelpers.ButtonHelpers;
import com.luffy.testautomation.utils.helpers.AppiumHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public abstract class BalanceInstalmentScreen extends BaseScreen
        implements BalanceInstalment, ButtonHelpers {

    protected ActionHelpers actionHelpers;

    protected By amountEligibleForInstalmentSection;
    protected By instalmentAmountEditBox;
    protected By offerCapBanner;
    protected By closeButtonOnOfferCapBanner;
    protected By inlineMessage;

    public BalanceInstalmentScreen(
            AppiumDriver appiumDriver, AppiumHelpers appiumHelpers, ActionHelpers actionHelpers) {
        super(appiumDriver, appiumHelpers);
        this.appiumDriver = appiumDriver;
        this.appiumHelpers = appiumHelpers;
        this.actionHelpers = actionHelpers;
    }

    @Override
    public boolean isVisible(Integer... timeoutInSeconds) {
        int timeout = timeoutInSeconds.length == 0 ? defaultTimeoutInSeconds : timeoutInSeconds[0];
        return appiumHelpers.checkForVisibilityOfElementsBy(
                amountEligibleForInstalmentSection, timeout);
    }

    @Override
    public void waitVisible() {
        appiumHelpers.waitForElement(amountEligibleForInstalmentSection);
    }

    @Override
    public boolean isOfferCapBannerDisplayed() {
        return appiumHelpers.checkForElement(offerCapBanner);
    }

    @Override
    public void clickCloseOnOfferCapBanner() {
        appiumHelpers.waitForElement(closeButtonOnOfferCapBanner).click();
    }

    @Override
    public boolean isInlineMessageDisplayed() {
        return appiumHelpers.checkForElement(inlineMessage);
    }
}
