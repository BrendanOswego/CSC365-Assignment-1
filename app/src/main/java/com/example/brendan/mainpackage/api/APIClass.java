package com.example.brendan.mainpackage.api;

import android.location.Location;

import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.DataSetEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.DataSetModel;
import com.example.brendan.mainpackage.model.LocationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * API class for all api calls using the interface class
 */

public class APIClass {

    private RetroInterface controller;

    public APIClass(){
        this.controller = RetroController.getServer();
    }

    public UUID getLocations(){
        final UUID uuid = UUID.randomUUID();
        Call<LocationModel> call = controller.getLocation();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if(response.isSuccessful()) {
                    event = new LocationEvent(uuid,response.body());
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
        return uuid;
    }
    public UUID getLocationById(String id){
        final UUID uuid = UUID.randomUUID();
        Call<LocationModel> call = controller.getLocationById(id);
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if(response.isSuccessful()) {
                    event = new LocationEvent(uuid,response.body());
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
        return uuid;
    }

    public UUID getDataSets(){
        final UUID uuid = UUID.randomUUID();
        Call<DataSetModel> call = controller.getDataSets();
        call.enqueue(new Callback<DataSetModel>() {
            @Override
            public void onResponse(Call<DataSetModel> call, Response<DataSetModel> response) {
                DataSetEvent event;
                if(response.isSuccessful()){
                    event = new DataSetEvent(uuid,response.body());
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
        return uuid;
    }

    public UUID getData(String id,String startdate,String enddata){
        final UUID uuid = UUID.randomUUID();
        Call<DataModel> call = controller.getData(id,startdate,enddata);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                DataEvent event;
                if(response.isSuccessful()){
                    event = new DataEvent(uuid,response.body());
                    EventBus.getDefault().post(event);
                }else{
                    int status = response.code();
                    System.out.println("Status Code: " + status);
                }
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
            t.printStackTrace();
            }
        });
        return uuid;
    }
}
