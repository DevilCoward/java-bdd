package com.luffy.testautomation.appautomation.apps.android;

import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.apps.OpenBanking;

public class OpenBankingApp extends AbstractAndroidApp
        implements OpenBanking, Provider<OpenBankingApp> {
    public OpenBankingApp() {
        super(
                "tpp",
                "com.hsbc.mobilebanking.trustedthirdpartyapp",
                "com.hsbc.mobilebanking.trustedthirdpartyapp.ScenarioActivity",
                "com.hsbc.mobilebanking.trustedthirdpartyapp");
    }

    @Override
    public OpenBankingApp get() {
        return this;
    }
}
