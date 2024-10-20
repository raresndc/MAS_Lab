package com.example.mihai.restapiexample.web.service;

import com.example.mihai.restapiexample.model.ObjectCollection;

import retrofit.Callback;
import retrofit.http.GET;

public interface DataService {
    //@GET("/templates/6aXJX9STJIot/data")
    @GET("/templates/dkYHziMbWywN/data")
    void getDataObjects(Callback<ObjectCollection> callback);
}
