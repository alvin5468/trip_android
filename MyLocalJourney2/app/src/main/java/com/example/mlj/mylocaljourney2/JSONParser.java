package com.example.mlj.mylocaljourney2;

import android.content.ContentValues;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONParser {
    private static JSONObject notOkJSON;
    private final static String TAG = "JSONParser";
    static {
        try {
            notOkJSON = new JSONObject("{\"isOk\" : false}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // constructor
    public JSONParser() {
    }

    OkHttpClient client = new OkHttpClient();

    public JSONObject getJSONFromUrl(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string());
            } else {
                return notOkJSON;
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notOkJSON;
    }

    public JSONObject postJSONFromUrl(String url, ContentValues nameValuePair) {
        try {
            FormEncodingBuilder builder = new FormEncodingBuilder();

            for (String key : nameValuePair.keySet()) {
                builder.add(key, nameValuePair.getAsString(key));
            }

            RequestBody formBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string());
            } else {
                return notOkJSON;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notOkJSON;
    }
}
