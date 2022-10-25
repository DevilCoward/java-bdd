package com.luffy.testautomation.appautomation.apps.ios;

import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.apps.MerchantDemo;

public class MerchantDemoApp extends AbstractIosApp
        implements MerchantDemo, Provider<MerchantDemoApp> {

    private static String tppName = "merchantDemo3";
    private static String bundleId = "com.mc.demomerchant.r3";

    public MerchantDemoApp() {
        super(tppName, bundleId);
    }

    @Override
    public MerchantDemoApp get() {
        return this;
    }
}
