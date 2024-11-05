package com.example.appranduser;

import com.example.appranduser.Entities.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig() {
        retrofit = new Retrofit.Builder().baseUrl("https://randomuser.me/").
                addConverterFactory(GsonConverterFactory.create()).build();
    }

    public UserService getUserService() {
        return this.retrofit.create(UserService.class);
    }
}
