package com.example.brendan.mainpackage.api;

import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.LocationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface class that formulates the structure of all API calls
 */

interface RetroInterface {

    @GET("data?limit=100&units=standard")
    Call<DataModel> getData(@Query("datasetid") String id,
                            @Query("datatypeid") String datatypeid,
                            @Query("locationid") String locationId,
                            @Query("startdate") String startdate,
                            @Query("enddate") String enddate);

    @GET("locations?locationcategoryid=ST&limit=52")
    Call<LocationModel> getAllStates();

    @GET("locations?limit=50")
    Call<LocationModel> getAllLocations();

}
