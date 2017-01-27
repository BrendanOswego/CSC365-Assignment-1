package com.example.brendan.mainpackage.api;

import com.example.brendan.mainpackage.model.LocationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by brendan on 1/27/17.
 */

public interface RetroInterface {

    @GET("locations?limit=1000")
    Call<LocationModel> getLocation();

    @GET("locations/{id}?limit=1000")
    Call<LocationModel> getLocationById(@Path("id")String id);



}
