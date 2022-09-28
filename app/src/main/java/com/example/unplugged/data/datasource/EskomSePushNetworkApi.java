package com.example.unplugged.data.datasource;

import android.app.Application;
import com.android.volley.Request;

import org.json.JSONException;

import java.util.HashMap;
import java.util.function.Consumer;

public class EskomSePushNetworkApi {

    private static final int GET = Request.Method.GET;
    private static final String REQUEST_FAILED = "REQUEST_FAILED";
    private static final String API_ERROR = "API_ERROR";
    private static final String BASE_URL = ""; //https://developer.sepush.co.za/business/2.0/
    private final VolleyRequestManager requestManager;

    public EskomSePushNetworkApi(Application application) {
        requestManager = VolleyRequestManager.getInstance(application);
    }

    public void getStatus(Consumer<String> onError, Consumer<String> onResponse) {
        final String URL = BASE_URL + "status";

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> {
                    try {
                        onResponse.accept(response.getJSONObject("status").getJSONObject("eskom").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(API_ERROR);
                    }
                },
                error -> onError.accept(REQUEST_FAILED)
        );
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    public void getAreaInfo(String id, Consumer<String> onError, Consumer<String> onResponse) {
        final String URL = BASE_URL + "area?id=" + id;

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> onResponse.accept(response.toString()),
                error -> onError.accept(REQUEST_FAILED)
        );
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    public void findAreas(String searchText, Consumer<String> onError, Consumer<String> onResponse) {
        final String URL = BASE_URL + "areas_search?text=" + searchText;

        ApiJsonObjectRequest request = new ApiJsonObjectRequest(GET, URL, null,
                response -> {
                    try {
                        onResponse.accept(response.getJSONArray("areas").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError.accept(API_ERROR);
                    }
                },
                error -> onError.accept((REQUEST_FAILED)
        ));
        request.setHeaders(getRequestHeaders());
        requestManager.addToRequestQueue(request);
    }

    private HashMap<String, String> getRequestHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Token", "");
        return headers;
    }
}
