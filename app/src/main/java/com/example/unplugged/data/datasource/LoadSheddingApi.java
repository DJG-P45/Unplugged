package com.example.unplugged.data.datasource;

import io.reactivex.rxjava3.core.Single;

public interface LoadSheddingApi {

    Single<String> getStatus();

    Single<String> getAreaInfo(String id);

    Single<String> findAreas(java.lang.String searchText);

}
