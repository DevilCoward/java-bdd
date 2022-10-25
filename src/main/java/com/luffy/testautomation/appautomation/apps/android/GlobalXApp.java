package com.luffy.testautomation.appautomation.apps.android;

import com.luffy.testautomation.appautomation.apps.GlobalX;

public class GlobalXApp extends AbstractAndroidApp implements GlobalX {

    public GlobalXApp() {
        super(
                "globalx",
                "ca.hsbc.hsbccanada.cert",
                "com.hsbc.mobilebanking.splash.SplashActivity",
                "com.hsbc.mobilebanking.steplauncher.StepLauncherActivity");
    }
}
