package com.luffy.testautomation.appautomation.apps.android;

import com.luffy.testautomation.appautomation.apps.OneFive;

public class OneFiveApp extends AbstractAndroidApp implements OneFive {

    public OneFiveApp() {
        super(
                "onefive",
                "com.htsu.hsbcpersonalbanking.test",
                "com.hsbc.activities.LaunchActivity",
                "com.hsbc.activities.EulaActivity");
    }
}
