package com.luffy.testautomation.appautomation.utilities.plugins.Cedar;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * CedarApi class provides the interface for interacting with the Cedar Reporting server. All
 * communication to the Cedar server shall be done through this class.
 */
public class CedarApi {
    private static final Logger log = LoggerFactory.getLogger(CedarApi.class);

    public String createNewRun(RunData runData) {
        JSONObject runParams = runData.toJson();
        log.info("Starting new test run");
        String endPoint = "runs/a/create";

        Response response = makeRequest(endPoint, runParams.toString());

        String uuid = null;
        try {
            JSONObject jsonResponse = new JSONObject(response.body().string());
            uuid = jsonResponse.get("uuid").toString();
            log.info("Retrieved UUID: " + uuid);
            response.body().close();
        } catch (IOException | NullPointerException | JSONException exception) {
            log.info("Error creating new run");
            log.error(getStackTrace(exception));
            log.error("Code: " + response.code());
        }

        return uuid;
    }

    /**
     * Update a test runs overall status given RunData
     *
     * @param runData RunData object containing data for the run to be updated
     * @return True if response occurred from Cedar API, else false
     */
    public boolean updateRun(RunData runData) {
        log.info("Updating Test Run");
        String endPoint = "runs/a/run";
        JSONObject jsonData = runData.toJson();
        Response response = makeRequest(endPoint, jsonData.toString());
        boolean status;
        try {
            status = response.isSuccessful();
            response.body().close();
        } catch (Exception exception) {
            log.error("Error Updating Run: " + exception);
            status = false;
        }
        return status;
    }

    /**
     * Update a runs test result data given RunData and TestData. Test Data is used to determine the
     * details for the result. ResultData is used to determine the Run the result is pertinent to.
     *
     * @param runData RunData object for which the result belongs
     * @param testData TestData object for which the result belongs
     */
    public void submitResult(RunData runData, TestData testData) {
        log.info("Updating Test Run: " + runData.uuid);
        String endPoint = "runs/a/result";

        JSONObject jsonData = testData.toJson();
        jsonData.put("run_uuid", runData.uuid);

        Response response = makeRequest(endPoint, jsonData.toString());
        try {
            response.body().close();
        } catch (Exception ignored) {
        }
    }

    /**
     * Update a test case on the Cedar DB
     *
     * @param testData TestData object holding details of the test to be updated
     * @return True if response occurred from the Cedar API, else False
     */
    public boolean updateTestCase(TestData testData) {
        log.info("Updating Test Case: " + testData.name);
        String endPoint = "tests/a/case/submit";
        JSONObject jsonData = testData.toJson();

        Response response = makeRequest(endPoint, jsonData.toString());
        return response != null && response.isSuccessful();
    }

    /**
     * Update a set of tests on the Cedar DB in bulk
     *
     * @param featureDataList List of features to be updated at once
     */
    public void bulkUpdateTestCases(List<FeatureData> featureDataList) {
        log.info("Updating cases in bulk");
        String endPoint = "tests/a/cases/update";

        JSONArray sourceJson = new JSONArray();
        for (FeatureData featureData : featureDataList) {
            for (TestData testData : featureData.testDataList) {
                sourceJson.put(testData.toJson());
            }
        }

        JSONArray batch = new JSONArray();
        for (int i = 0; i < sourceJson.length(); i++) {
            batch.put(sourceJson.get(i));

            if (batch.length() == 50 || i == sourceJson.length()) {
                makeRequest(endPoint, sourceJson.toString());
                batch = new JSONArray();
            }
        }
    }

    /**
     * Make a post request to the Cedar API. This method is to be used by other methods in this
     * class to facilitate communication with the Cedar API.
     *
     * @param endPoint The endpoint on Cedar to Post to
     * @param jsonData JSON data as a string to be posted
     * @return Response object, directly from the client request.
     */
    private Response makeRequest(String endPoint, String jsonData) {
        MediaType jsonMedia = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonMedia, jsonData);
        OkHttpClient okHttpClient = new OkHttpClient();

        String apiBaseUrl = "cedar.build-mobile.euw1.dev.aws.cloud.hsbc";
        int port = 80;
        String urlScheme = "http";
        HttpUrl url =
                new HttpUrl.Builder()
                        .scheme(urlScheme)
                        .host(apiBaseUrl)
                        .port(port)
                        .addPathSegment(endPoint)
                        .build();

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            log.info("Response: " + response.body());
            return response;
        } catch (IOException e) {
            log.info("Error: " + e);
        }
        return null;
    }
}
