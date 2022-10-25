package com.luffy.testautomation.appautomation.config;

import com.google.inject.AbstractModule;
import com.luffy.testautomation.appautomation.apps.*;
import com.luffy.testautomation.appautomation.apps.android.*;
import com.luffy.testautomation.appautomation.contracts.instalments.*;
import com.luffy.testautomation.appautomation.contracts.wandoujia.Home;
import com.luffy.testautomation.appautomation.contracts.wandoujia.MyPea;
import com.luffy.testautomation.appautomation.screens.android.instalments.*;
import com.luffy.testautomation.appautomation.screens.android.wandoujia.HomeScreenAndroid;
import com.luffy.testautomation.appautomation.screens.android.wandoujia.MyPeaScreenAndroid;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionsAndroid;
import com.luffy.testautomation.utils.helpers.deviceactionhelpers.DeviceActionHelpers;
import com.luffy.testautomation.utils.helpers.deviceactionhelpers.DeviceActionsAndroid;

public class AbstractAndroidModule extends AbstractModule {

    @Override
    protected void configure() {
        // Apps
        bind(GlobalX.class).to(GlobalXApp.class);
        bind(OneFive.class).to(OneFiveApp.class);
        // Helpers
        bind(ActionHelpers.class).to(ActionsAndroid.class);
        bind(DeviceActionHelpers.class).toProvider(DeviceActionsAndroid.class);
        // Wandoujia
        bind(Home.class).toProvider(HomeScreenAndroid.class);
        bind(MyPea.class).toProvider(MyPeaScreenAndroid.class);
        // Instalment MY
        bind(BalanceInstalment.class).toProvider(BalanceInstalmentScreenAndroid.class);
        bind(CreateInstalment.class).toProvider(CreateInstalmentScreen.class);
        bind(InstalmentMultipleOnboarding.class).toProvider(InstalmentMultipleOnboardingScreenAndroid.class);
        bind(InstalmentOnboarding.class).toProvider(InstalmentOnboardingScreen.class);
        bind(InstalmentSampleAppLogin.class).toProvider(InstalmentSampleAppLoginScreen.class);
    }
}
