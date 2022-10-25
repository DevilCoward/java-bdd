package com.luffy.testautomation.appautomation.config;

import java.util.Optional;

public class Environment {

    public static String requiredSystemProperty(String name) {
        return Optional.ofNullable(System.getProperty(name))
                .map(String::toUpperCase)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "You didn't define System Property '" + name + "'"));
    }
}
