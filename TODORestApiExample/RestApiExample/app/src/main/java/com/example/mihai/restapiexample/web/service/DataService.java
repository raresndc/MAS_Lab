package com.example.mihai.restapiexample.web.service;

import com.example.mihai.restapiexample.model.DataObject;
import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;

public interface DataService {
    @GET("/templates/dkYHziMbWywN/data")
    void getDataObjects(Callback<List<DataObject>> callback);
}
