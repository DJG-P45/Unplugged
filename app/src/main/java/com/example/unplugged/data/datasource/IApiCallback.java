package com.example.unplugged.data.datasource;

public interface IApiCallback {
    void onResponse(String json) throws Exception;
    void onError(String error);
}
