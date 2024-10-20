package com.example.mihai.restapiexample.web.adapter;

import com.example.mihai.restapiexample.web.service.DataService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class RestApiWebAdapter {

    private static final String TOKEN = "Bearer kg1sjtdu5nwp8i5dfaa9hsedcvkp79zayefyha84";

    public DataService getData() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.json-generator.com/")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "*/*");
                        request.addHeader("Authorization", TOKEN);
                    }
                })
                .build();
        return restAdapter.create(DataService.class);
    }

}
