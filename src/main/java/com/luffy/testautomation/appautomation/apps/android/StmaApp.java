package com.luffy.testautomation.appautomation.apps.android;

import com.luffy.testautomation.appautomation.apps.Stma;

public class StmaApp extends AbstractAndroidApp implements Stma {

    public StmaApp() {
        super(
                "stma",
                "hk.com.hsbc.hsbchkeasyinvest.dev",
                "com.hsbc.mobile.stocktrading.general.activity.SplashScreenActivity",
                "hk.com.hsbc.hsbchkeasyinvest.dev");
    }
}
