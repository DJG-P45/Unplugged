package com.example.unplugged.data.datasource;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestManager {

    private static VolleyRequestManager instance;
    private RequestQueue requestQueue;

    private VolleyRequestManager() {
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestManager getInstance(Application application) {
        if (instance == null) {
            instance = new VolleyRequestManager();
            instance.requestQueue = Volley.newRequestQueue(application);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
