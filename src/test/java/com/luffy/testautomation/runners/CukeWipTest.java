package com.luffy.testautomation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:build/reports/tests/cucumber-extent/cucumber_report.json"
        },
        junit = "--step-notifications",
        features = {"src/test/resources"},
        tags = {"@luffy"},
        glue = {
                "com.luffy.testautomation.steps",
                "com.luffy.testautomation.appautomation.utilities"
        })
public class CukeWipTest {
}
