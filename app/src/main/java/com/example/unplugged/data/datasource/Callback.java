package com.example.unplugged.data.datasource;

public interface Callback {
    void onResponse(String json);
    void onError(String error);
}
