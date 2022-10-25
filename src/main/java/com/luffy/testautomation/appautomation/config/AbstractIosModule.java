package com.luffy.testautomation.appautomation.config;

import com.google.inject.AbstractModule;
import com.luffy.testautomation.appautomation.apps.*;
import com.luffy.testautomation.appautomation.apps.ios.*;
import com.luffy.testautomation.appautomation.contracts.instalments.*;
import com.luffy.testautomation.appautomation.contracts.wandoujia.Home;
import com.luffy.testautomation.appautomation.contracts.wandoujia.MyPea;
import com.luffy.testautomation.appautomation.screens.ios.instalments.*;
import com.luffy.testautomation.appautomation.screens.ios.wandoujia.HomeScreenIos;
import com.luffy.testautomation.appautomation.screens.ios.wandoujia.MyPeaScreenIos;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionHelpers;
import com.luffy.testautomation.utils.helpers.actionHelpers.ActionsIos;
import com.luffy.testautomation.utils.helpers.deviceactionhelpers.DeviceActionHelpers;
import com.luffy.testautomation.utils.helpers.deviceactionhelpers.DeviceActionsIos;

public class AbstractIosModule extends AbstractModule {

    @Override
    protected void configure() {
        // Apps
        bind(GlobalX.class).to(GlobalXApp.class);
        bind(OneFive.class).to(OneFiveApp.class);
        // Helpers
        bind(ActionHelpers.class).to(ActionsIos.class);
        bind(DeviceActionHelpers.class).toProvider(DeviceActionsIos.class);
        // Wandoujia
        bind(Home.class).toProvider(HomeScreenIos.class);
        bind(MyPea.class).toProvider(MyPeaScreenIos.class);
        // Instalment MY
        bind(BalanceInstalment.class).toProvider(BalanceInstalmentScreenIos.class);
        bind(CreateInstalment.class).toProvider(CreateInstalmentScreen.class);
        bind(InstalmentMultipleOnboarding.class).toProvider(InstalmentMultipleOnboardingScreenIos.class);
        bind(InstalmentOnboarding.class).to(InstalmentOnboardingScreen.class);
        bind(InstalmentSampleAppLogin.class).toProvider(InstalmentSampleAppLoginScreen.class);
    }
}
