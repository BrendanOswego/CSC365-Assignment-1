package com.example.brendan.mainpackage.api;

import android.location.Location;

import com.example.brendan.mainpackage.event.DataSetEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataSetModel;
import com.example.brendan.mainpackage.model.LocationModel;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brendan on 1/27/17.
 */

public class APIClass {

    private RetroInterface controller;

    public APIClass(){
        this.controller = RetroController.getServer();
    }

    public void getLocations(){
    Call<LocationModel> call = controller.getLocation();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if(response.isSuccessful()) {
                    event = new LocationEvent(response.body());
                    EventBus.getDefault().post(event);
                }else {
                    int status = response.code();
                    System.out.println("Status Code: "+status);
                }

            }
            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
    public void getLocationById(String id){
        Call<LocationModel> call = controller.getLocationById(id);
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if(response.isSuccessful()) {
                    event = new LocationEvent(response.body());
                    EventBus.getDefault().post(event);
                }else {
                    int status = response.code();
                    System.out.println("Status Code: "+status);
                }

            }
            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public void getDataSets(){
        Call<DataSetModel> call = controller.getDataSets();
        call.enqueue(new Callback<DataSetModel>() {
            @Override
            public void onResponse(Call<DataSetModel> call, Response<DataSetModel> response) {
                DataSetEvent event;
                if(response.isSuccessful()){
                    event = new DataSetEvent(response.body());
                    EventBus.getDefault().post(event);
                }else {
                    int status = response.code();
                    System.out.println("Status Code: " + status);
                }
            }

            @Override
            public void onFailure(Call<DataSetModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
