package com.luffy.testautomation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        features = {"src/test/tests"},
        tags = {"@usa"},
        glue = {
                "com.hsbc.digital.testautomation.global.mobilextestautomation.steps",
                "com.hsbc.digital.testautomation.global.mobilextestautomation.utilities"
        })
public class CustomCukeWipTest {
}
