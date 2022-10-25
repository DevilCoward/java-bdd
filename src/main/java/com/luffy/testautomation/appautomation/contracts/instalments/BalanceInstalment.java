package com.luffy.testautomation.appautomation.contracts.instalments;

import com.luffy.testautomation.appautomation.contracts.VisibleInterface;

public interface BalanceInstalment extends VisibleInterface {

    boolean isOfferCapBannerDisplayed();

    void inputInstalmentAmount(String instalmentAmount);

    void clickCloseOnOfferCapBanner();

    boolean isInlineMessageDisplayed();
}
