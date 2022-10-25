package com.luffy.testautomation.appautomation.apps.ios;

import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.apps.OpenBanking;

public class OpenBankingApp extends AbstractIosApp
        implements OpenBanking, Provider<OpenBankingApp> {

    private static String tppName = "tpp";
    private static String bundleId =
            "uk.co.hsbc.enterprise.hsbcmobilebanking.sample.openbankingapp";

    public OpenBankingApp() {
        super(tppName, bundleId);
    }

    @Override
    public OpenBankingApp get() {
        return this;
    }
}
