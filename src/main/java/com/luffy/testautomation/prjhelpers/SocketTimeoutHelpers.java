package com.luffy.testautomation.prjhelpers;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class SocketTimeoutHelpers {
    // to solve java.net.SocketTimeoutException: timeout
    public static OkHttpClient getOkHttpClient(int timeoutDuration, TimeUnit timeoutUnit) {
        OkHttpClient client =
                new OkHttpClient.Builder()
                        .connectTimeout(timeoutDuration, timeoutUnit) // connect timeout
                        .writeTimeout(timeoutDuration, timeoutUnit) // write timeout
                        .readTimeout(timeoutDuration, timeoutUnit) // read timeout
                        .build();
        return client;
    }
}
