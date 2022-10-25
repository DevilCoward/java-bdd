package com.luffy.testautomation.appautomation.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * A utility to provide retry wrapper for functional interfaces. (e.g. Runnable, Supplier<T>,
 * Consumer<T>, Function<T,R> etc)
 */
public class RetryUtil {

    private static final Integer defaultRetryCount = 3;
    private static final Logger log = LoggerFactory.getLogger(RetryUtil.class);

    /**
     * If a function throws an exception, retry itself for default 3 times at most.
     *
     * @param func A function to call.
     * @param param A parameter that will be applied to the function.
     * @param <T> Type of the function's parameter.
     * @param <R> Type of the function's return value.
     * @return Null if the function throws error after retrying, otherwise returns the function
     *     result.
     */
    public static <T, R> R retry(Function<T, R> func, T param) {
        return retry(func, param, defaultRetryCount);
    }

    /**
     * If a function throws an exception, retry itself for specified times at most.
     *
     * @param func A function to call.
     * @param param A parameter that will be applied to the function.
     * @param retryCount Retry times that will be performed at most.
     * @param <T> Type of the function's parameter.
     * @param <R> Type of the function's return value.
     * @return Null if the function throws error after retrying, otherwise returns the function
     *     result.
     */
    public static <T, R> R retry(Function<T, R> func, T param, Integer retryCount) {
        return retry(func, param, func, param, retryCount);
    }

    /**
     * If a function throws an exception, retry a new function instead for default 3 times at most.
     *
     * @param oldFunc An old function to call.
     * @param oldParam A parameter that will be applied to the old function.
     * @param newFunc A new function to call.
     * @param newParam A parameter that will be applied to the new function.
     * @param <T> Type of the old function's parameter.
     * @param <U> Type of the new function's parameter.
     * @param <R> Type of the old/new function's return value.
     * @return Null if the new function throws error after retrying, otherwise returns the old/new
     *     function result.
     */
    public static <T, U, R> R retry(
            Function<T, R> oldFunc, T oldParam, Function<U, R> newFunc, U newParam) {
        return retry(oldFunc, oldParam, newFunc, newParam, defaultRetryCount);
    }

    /**
     * If a function throws an exception, retry a new function instead for specified times at most.
     *
     * @param oldFunc An old function to call.
     * @param oldParam A parameter that will be applied to the old function.
     * @param newFunc A new function to call.
     * @param newParam A parameter that will be applied to the new function.
     * @param retryCount Retry times that will be performed at most.
     * @param <T> Type of the old function's parameter.
     * @param <U> Type of the new function's parameter.
     * @param <R> Type of the old/new function's return value.
     * @return Null if the new function throws error after retrying, otherwise returns the old/new
     *     function result.
     */
    public static <T, U, R> R retry(
            Function<T, R> oldFunc,
            T oldParam,
            Function<U, R> newFunc,
            U newParam,
            Integer retryCount) {
        try {
            return oldFunc.apply(oldParam);
        } catch (Exception ex) {
            log.info(getStackTrace(ex));
            retryCount--;
        }

        while (retryCount > 0) {
            try {
                return newFunc.apply(newParam);
            } catch (Exception ex) {
                log.info(getStackTrace(ex));
                retryCount--;
            }
        }
        return null;
    }
}
