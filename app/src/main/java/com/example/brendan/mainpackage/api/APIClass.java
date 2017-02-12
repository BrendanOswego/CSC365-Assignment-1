package com.example.brendan.mainpackage.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.brendan.mainpackage.BaseFragment;
import com.example.brendan.mainpackage.MainActivity;
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

import static android.support.v7.appcompat.R.styleable.View;


/**
 * API class for all api calls using the interface class
 */

public class APIClass {

    private RetroInterface controller;

    private static APIClass instance;
    ProgressDialog dialog;
    Context context;
    ProgressDialog loading;

    public static APIClass getInstance() {
        if (instance == null) {
            instance = new APIClass();
        }
        return instance;
    }

    private APIClass() {

        this.controller = RetroController.getServer();
    }

    public void init(Context context) {
        this.context = context;
    }


    public UUID getLocations() {
        final UUID uuid = UUID.randomUUID();
        Call<LocationModel> call = controller.getLocation();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if (response.isSuccessful()) {
                    event = new LocationEvent(uuid, response.body());
                    EventBus.getDefault().post(event);
                } else {
                    int status = response.code();
                    System.out.println("Status Code: " + status);
                }

            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return uuid;
    }

    public UUID getLocationById(String id) {
        final UUID uuid = UUID.randomUUID();
        Call<LocationModel> call = controller.getLocationById(id);
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if (response.isSuccessful()) {
                    event = new LocationEvent(uuid, response.body());
                } else {
                    int status = response.code();
                    System.out.println("Status Code: " + status);
                    event = new LocationEvent(uuid, null);
                }
                EventBus.getDefault().post(event);

            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return uuid;
    }

    public UUID getDataSets() {
        final UUID uuid = UUID.randomUUID();
        Call<DataSetModel> call = controller.getDataSets();
        call.enqueue(new Callback<DataSetModel>() {
            @Override
            public void onResponse(Call<DataSetModel> call, Response<DataSetModel> response) {
                DataSetEvent event;
                if (response.isSuccessful()) {
                    event = new DataSetEvent(uuid, response.body());
                } else {
                    int status = response.code();
                    event = new DataSetEvent(uuid, null);
                    System.out.println("Status Code: " + status);
                }
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<DataSetModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return uuid;
    }

    public UUID getData(String id, String dataType,String locationId, String startdate, String enddata) {
        final UUID uuid = UUID.randomUUID();
        Call<DataModel> call = controller.getData(id,dataType, locationId, startdate, enddata);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                DataEvent event;
                if (response.isSuccessful()) {
                    event = new DataEvent(uuid, response.body());
                } else {
                    int status = response.code();
                    event = new DataEvent(uuid, null);
                    System.out.println("Status Code: " + status);
                }
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return uuid;
    }

    public UUID getAllStates() {
        showDialog("Fetching Locations");
        final UUID uuid = UUID.randomUUID();
        Call<LocationModel> call = controller.getAllStates();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                LocationEvent event;
                if (response.isSuccessful()) {
                    event = new LocationEvent(uuid, response.body());
                } else {
                    int status = response.code();
                    event = new LocationEvent(uuid, null);
                    System.out.println("Status Code: " + status);
                }
                closeDialog();
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return uuid;
    }

    public void showDialog(String title) {
        if (loading == null) {
            loading = new ProgressDialog(context);
        }
        loading.setTitle(title);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.setMessage("Please wait...this will take some time");
        loading.show();
    }

    public void closeDialog() {
        loading.dismiss();
    }
}
