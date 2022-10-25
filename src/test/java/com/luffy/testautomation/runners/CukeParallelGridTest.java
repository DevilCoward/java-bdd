package com.luffy.testautomation.runners;

import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

public class CukeParallelGridTest {

    @Test
    public void runTestParallel() throws Exception {
        /* Create runner class instances as per the device grid */
        /* Set Runner Class and Parallel Instances */
        Class<?>[] runnerClasses =
                createRunnerClassThread(com.luffy.testautomation.runners.CustomCukeWipTest.class, getParallelInstances());
        /* Run Test Parallel */
        JUnitCore.runClasses(new ParallelComputer(true, true), runnerClasses);
    }

    private Class<?>[] createRunnerClassThread(Class klass, int runnerThreads) {
        Class<?>[] runnerClasses = new Class[runnerThreads];
        for (int index = 0; index < runnerThreads; index++) runnerClasses[index] = klass;

        System.out.println("Grid Instances : " + runnerClasses.length);
        return runnerClasses;
    }

    private int getParallelInstances() {
        String platform = System.getProperty("platform") + ".grid";
        String deviceGrid = ConfigFactory.parseResources("devices.conf").getString(platform);
        return deviceGrid.split(",").length;
    }
}
