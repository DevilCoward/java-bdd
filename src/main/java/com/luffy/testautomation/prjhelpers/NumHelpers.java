package com.luffy.testautomation.prjhelpers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class NumHelpers {
    public static double generateRandomFloatNum(double leftLimit, double rightLimit) {
        return formatDecimalValue(leftLimit + new Random().nextDouble() * (rightLimit - leftLimit));
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than or equal to min");
        }
        Random randomAmount = new Random();
        return randomAmount.nextInt((max - min) + 1) + min;
    }

    public static int generateRandomNumberWithExcepts(int start, int end, List<Integer> excepts) {
        int size = excepts.size();
        Random rand = new Random();
        int range = end - start + 1 - size;
        int randNum = rand.nextInt(range) + start;
        excepts.sort(null);
        int i = 0;
        for (int except : excepts) {
            if (randNum < except - i) {
                return randNum + i;
            }
            i++;
        }
        return randNum + i;
    }

    public static double formatDecimalValue(double value) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return Double.parseDouble(df.format(value));
    }
}
