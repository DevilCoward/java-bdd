package com.luffy.testautomation.appautomation.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.luffy.testautomation.prjhelpers.LocaleCSVParser;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyInjector implements InjectorSource {
    private final Logger log = LoggerFactory.getLogger(MyInjector.class);

    LocaleCSVParser localeCSVParser = new LocaleCSVParser();

    @Override
    public Injector getInjector() {
        String chosenPlatform = Environment.requiredSystemProperty("platform");
        String env = Environment.requiredSystemProperty("env");
        String country = Environment.requiredSystemProperty("country");
        String locale = Environment.requiredSystemProperty("locale");
        localeCSVParser.createMapWithLocalisedValues();

        String gridOption = System.getProperty("grid");
        if (Boolean.parseBoolean(gridOption)) chosenPlatform = chosenPlatform + "GRID";
        log.info(
                String.format(
                        "Running tests on '%s' for environment '%s' and entity '%s' with locale '%s'",
                        chosenPlatform, env, country, locale));
        return Guice.createInjector(
                Stage.PRODUCTION,
                new com.luffy.testautomation.appautomation.config.MyFrameworkModule(),
                CucumberModules.createScenarioModule(),
                com.luffy.testautomation.appautomation.config.PlatformModule.valueOf(chosenPlatform).getModule());
    }
}
