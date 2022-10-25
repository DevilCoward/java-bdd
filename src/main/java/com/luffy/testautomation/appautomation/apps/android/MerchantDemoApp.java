package com.luffy.testautomation.appautomation.apps.android;

import com.google.inject.Provider;
import com.luffy.testautomation.appautomation.apps.MerchantDemo;

public class MerchantDemoApp extends AbstractAndroidApp
        implements MerchantDemo, Provider<MerchantDemoApp> {
    public MerchantDemoApp() {
        super(
                "merchantDemo3",
                "uk.co.zapp.samplezappmerchantapp3.release",
                "uk.co.zapp.samplezappmerchantapp.MainActivity",
                "uk.co.zapp.samplezappmerchantapp3.release");
    }

    @Override
    public MerchantDemoApp get() {
        return this;
    }
}
