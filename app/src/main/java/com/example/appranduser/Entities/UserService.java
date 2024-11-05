package com.example.appranduser.Entities;

import com.example.appranduser.Entities.Api.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/")
    Call<Users> getRandomUser(@Query("gender") String genero, @Query("nat") String nacionalidade);
}
