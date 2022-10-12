package com.example.unplugged.data.datasource;

import android.app.Application;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;

public class EskomSePushNetworkApi implements LoadSheddingApi {

    private static final int GET = Request.Method.GET;
    private static final String BASE_URL = "https://developer.sepush.co.za/business/2.0/";
    private final VolleyRequestManager requestManager;
    private final String API_KEY;

    public EskomSePushNetworkApi(Application application, String apiKey) {
        requestManager = VolleyRequestManager.getInstance(application);
        this.API_KEY = apiKey;
    }

    @Override
    public Single<String> getStatus() {
        return Single.create(emitter -> {
            Response.Listener<JSONObject> responseListener = response -> {
                try {
                    emitter.onSuccess(response.getJSONObject("status").getJSONObject("eskom").toString());
                } catch (JSONException e) {
                    emitter.onError(new ApiException());
                }
            };

            Response.ErrorListener errorListener = error -> emitter.onError(new ApiNetworkException());

            final String URL = BASE_URL + "status";
            sendRequest(GET, URL, null, responseListener, errorListener);
        });
    }

    @Override
    public Single<String> getAreaInfo(String id) {
        return Single.create(emitter -> {
            Response.Listener<JSONObject> responseListener = response -> emitter.onSuccess(response.toString());
            Response.ErrorListener errorListener = error -> emitter.onError(new ApiNetworkException());

            final String URL = BASE_URL + "area?id=" + id;
            sendRequest(GET, URL, null, responseListener, errorListener);
        });
    }

    @Override
    public Single<String> findAreas(String searchText) {
        return Single.create(emitter -> {
            Response.Listener<JSONObject> responseListener = response -> {
                try {
                    emitter.onSuccess(response.getJSONArray("areas").toString());
                } catch (JSONException e) {
                    emitter.onError(new ApiException());
                }
            };

            Response.ErrorListener errorListener = error -> emitter.onError(new ApiNetworkException());

            final String URL = BASE_URL + "areas_search?text=" + searchText;
            sendRequest(GET, URL, null, responseListener, errorListener);
        });
    }

    private synchronized void sendRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        ApiJsonObjectRequest request = new ApiJsonObjectRequest(method, url, jsonRequest, listener, errorListener);
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    private synchronized HashMap<String, String> getRequestHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Token", API_KEY);
        return headers;
    }
}
