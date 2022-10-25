package com.luffy.testautomation.appautomation.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.luffy.testautomation.appautomation.appiumDriver.AppiumDriverProvider;
import com.luffy.testautomation.utils.helpers.Defaults;
import com.typesafe.config.*;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFrameworkModule extends AbstractModule {

    private final Logger log = LoggerFactory.getLogger(MyFrameworkModule.class);

    @Override
    protected void configure() {
        bind(AppiumDriver.class).toProvider(AppiumDriverProvider.class);
    }

    @Provides
    public WebDriverWait provideWebdriverWait(AppiumDriver appiumDriver) {
        return new WebDriverWait(appiumDriver, Defaults.WAIT_TIME.getTime());
    }

    // FIXME: Add a singleton provider for http client which automatically uses these values
    private Config getProxyConfig() {
        Map<String, String> proxySettings = new HashMap<>();
        String proxyRegex =
                "http://(?:(?<user>[^:]*):(?<pass>[^@]*)@)*(?<domain>[^:]*):(?<port>[0-9]*)";
        Pattern regexPattern = Pattern.compile(proxyRegex);
        // ToDo - Added here for temporary time to resolve the issue with PR builds
        String httpProxy = "http://localhost:3128";
        Matcher httpMatcher = regexPattern.matcher(httpProxy);
        if (httpMatcher.find()) {
            proxySettings.put("http.proxyHost", httpMatcher.group("domain"));
            proxySettings.put("http.proxyPort", httpMatcher.group("port"));
        } else {
            throw new RuntimeException("Could not determine http proxy from system environment");
        }
        // ToDo - Added here for temporary time to resolve the issue with PR builds
        String httpsProxy = "http://localhost:3128";
        Matcher httpsMatcher = regexPattern.matcher(httpsProxy);
        if (httpsMatcher.find()) {
            proxySettings.put("https.proxyHost", httpsMatcher.group("domain"));
            proxySettings.put("https.proxyPort", httpsMatcher.group("port"));
        } else {
            throw new RuntimeException("Could not determine https proxy from system environment");
        }
        return ConfigFactory.parseMap(proxySettings);
    }

    private Config getCredentialConfig() {
        Map<String, String> credentialSettings = new HashMap<>();
        // Managed machines have user credentials written to the default gradle.properties file
        File gradleDefaultConfigFile =
                new File(System.getenv("HOME") + "/.gradle/gradle.properties");
        // We need to use special options to parse .properties files
        ConfigParseOptions parseOptions =
                ConfigParseOptions.defaults().setSyntax(ConfigSyntax.PROPERTIES);
        Config gradleDefaultConfig = ConfigFactory.parseFile(gradleDefaultConfigFile, parseOptions);
        try {
            credentialSettings.put("user.id", gradleDefaultConfig.getString("nexusUser"));
            credentialSettings.put("user.password", gradleDefaultConfig.getString("nexusPassword"));
        } catch (ConfigException e) {
            log.warn(
                    "Couldn't find nexus credentials in gradle config. Credentials will have to be set either as environment variables or in application.conf");
        }
        return ConfigFactory.parseMap(credentialSettings);
    }

    @Provides
    @Singleton
    public Config configProvider() {
        String countryConfigPath = "";
        Config proxyConfig = getProxyConfig();
        Config credentialsConfig = getCredentialConfig();
        // Below condition is a temporary solution after migration (SAAS to mAuth) condition  will
        // be removed
        if ("uk".equals(System.getProperty("country"))
                && "mauth".equals(System.getProperty("loginType"))) {
            countryConfigPath =
                    String.format(
                            "config/%s/%s_env_mauth.conf",
                            System.getProperty("country"), System.getProperty("env"));
        } else {
            // Pick config based on Country -> Env
            // Separate configuration files for environments (if needed)
            countryConfigPath =
                    String.format(
                            "config/%s/%s_env.conf",
                            System.getProperty("country"), System.getProperty("env"));
        }

        Config countryConfig = ConfigFactory.parseResources(countryConfigPath);
        Config defaultConfig = ConfigFactory.parseResources("application.conf");

        Config config =
                ConfigFactory.systemEnvironment()
                        .withFallback(ConfigFactory.systemProperties())
                        .withFallback(proxyConfig)
                        .withFallback(credentialsConfig)
                        .withFallback(countryConfig)
                        .withFallback(defaultConfig)
                        .resolve();

        log.info("RESOLVED environment  as: " + config.getString("environment"));
        return config;
    }
}
