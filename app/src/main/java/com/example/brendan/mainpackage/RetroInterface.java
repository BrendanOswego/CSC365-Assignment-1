package com.example.brendan.mainpackage;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by brendan on 1/27/17.
 */

public interface RetroInterface {

    @GET("locations")
    Call<LocationEvent> getLocation();

}
