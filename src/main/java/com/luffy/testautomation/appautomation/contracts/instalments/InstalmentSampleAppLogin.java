package com.luffy.testautomation.appautomation.contracts.instalments;

import com.luffy.testautomation.appautomation.contracts.VisibleInterface;

public interface InstalmentSampleAppLogin extends VisibleInterface {

    void selectEntity();

    void clickContinueButton();

    void selectActivePlans(String activePlansType);

    void clickLaunchInstalmentButtonOnSampleApp();
}
