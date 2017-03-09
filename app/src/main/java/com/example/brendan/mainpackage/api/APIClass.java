package com.example.brendan.mainpackage.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataModel;
import com.example.brendan.mainpackage.model.LocationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * API class for all api calls using the RetroInterface class
 */

public class APIClass {

    private RetroInterface controller;

    private static APIClass instance;
    private Context context;
    private ProgressDialog loading;

    /**
     * Singleton instance for calling APIClass.
     *
     * @return APIClass instance.
     */
    public static APIClass getInstance() {
        if (instance == null) {
            instance = new APIClass();
        }
        return instance;
    }

    /**
     * Private constructor for APIClass initialization.
     */
    private APIClass() {

        this.controller = RetroController.getServer();
    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * Gets Data from Web Service given by the specified parameters
     *
     * @param id         Data returned will be from the dataset specified.
     * @param dataType   Data returned will contain all of the data type(s) specified.
     * @param locationId Data returned will contain data for the location(s) specified.
     * @param startdate  Data returned will be after the specified date.
     * @param enddata    Data returned will be after the specified date.
     * @return randomly generated UUID.
     */
    public UUID getData(String id, String dataType, String locationId, String startdate, String enddata) {
        final UUID uuid = UUID.randomUUID();
        Call<DataModel> call = controller.getData(id, dataType, locationId, startdate, enddata);
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

    /**
     * Gets all information for all 52 States.
     *
     * @return randomly generated UUID.
     */
    public UUID getAllStates() {
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

    /**
     * Dialog method for APIClass when calls are waiting for a response.
     *
     * @param title String shown to user.
     */
    public void showDialog(String title, boolean data) {

        loading = new ProgressDialog(context);

        if (data) {
            loading.setProgress(0);
            loading.setMax(14);
            loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        } else {
            loading.setMessage("Fetching locations...");
        }
        loading.setTitle(title);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }

    /**
     * Hides dialog when a response has been made from the Web Service.
     */
    public void closeDialog() {
        loading.dismiss();
    }

    public ProgressDialog getDialog() {
        return loading;
    }
}
