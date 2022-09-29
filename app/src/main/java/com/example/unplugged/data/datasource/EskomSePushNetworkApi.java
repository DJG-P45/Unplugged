package com.example.unplugged.data.datasource;

import android.app.Application;
import com.android.volley.Request;

import org.json.JSONException;

import java.util.HashMap;

public class EskomSePushNetworkApi {

    private static final int GET = Request.Method.GET;
    private static final String REQUEST_FAILED = "REQUEST_FAILED";
    private static final String API_ERROR = "API_ERROR";
    private static final String BASE_URL = "https://developer.sepush.co.za/business/2.0/"; //https://developer.sepush.co.za/business/2.0/
    private final VolleyRequestManager requestManager;

    public EskomSePushNetworkApi(Application application) {
        requestManager = VolleyRequestManager.getInstance(application);
    }

    public void getStatus(IApiCallback callback) {
        final String URL = BASE_URL + "status";

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> {
                    try {
                        callback.onResponse(response.getJSONObject("status").getJSONObject("eskom").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError(API_ERROR);
                    }
                },
                error -> callback.onError(REQUEST_FAILED)
        );
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    public void getAreaInfo(String id, IApiCallback callback) {
        final String URL = BASE_URL + "area?id=" + id;

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> callback.onResponse(response.toString()),
                error -> callback.onError(REQUEST_FAILED)
        );
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    public void findAreas(String searchText, IApiCallback callback) {
        final String URL = BASE_URL + "areas_search?text=" + searchText;

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> {
                    try {
                        callback.onResponse(response.getJSONArray("areas").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError(API_ERROR);
                    }
                },
                error -> callback.onError((REQUEST_FAILED)
        ));
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    private HashMap<String, String> getRequestHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Token", "Q2ORxpAXTgkVRFV6UdJj");
        return headers;
    }
}
