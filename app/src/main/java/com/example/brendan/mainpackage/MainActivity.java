package com.example.brendan.mainpackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataEvent;
import com.example.brendan.mainpackage.event.DataSetEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.DataSetModel;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.model.ResultDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.UUID;

//REMINDER handle overflow of unnecessary information, check if count or limit is smaller

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private APIClass api;
    private List<Result> locationResults;
    private List<ResultDataSet> dataSetResults;
    private List<DataResults> dataResults;
    private int dataSetCount;
    private int locationCount;
    private int dataLimit;
    private int dataCount;
    private int locationLimit;
    private String firstId;
    private UUID locationUuid;
    private UUID datasetUuid;
    private UUID dataUuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        api = new APIClass();
        //FIXME: For now just call in onCreate, will fix later
        locationUuid = api.getLocations();
        datasetUuid = api.getDataSets();


    }
    @Subscribe
    public void onLocationEvent(LocationEvent event){
        if(event.getUuid().equals(this.locationUuid)) {
            this.locationResults = event.getLocation().getResults();
            this.locationCount = event.getLocation().getMetadata().getResultset().getCount();
            this.locationLimit = event.getLocation().getMetadata().getResultset().getLimit();
            showLocationResults();
        }
    }
    @Subscribe
    public void onDataSetEvent(DataSetEvent event){
        if(event.getUuid().equals(this.datasetUuid)) {
            this.dataSetCount = event.getDataSet().getMetadata().getResultset().getCount();
            this.dataSetResults = event.getDataSet().getResults();
            showDatasetResults();
            firstId = dataSetResults.get(0).getId();
            //setupData();
        }

    }
    @Subscribe
    public void onDataEvent(DataEvent event){
        if(event.getUuid().equals(this.dataUuid)){
            Log.v(TAG,"DATA EVENT");
            this.dataResults = event.getDataModel().getResults();
            this.dataLimit = event.getDataModel().getMetadata().getResultset().getLimit();
            this.dataCount = event.getDataModel().getMetadata().getResultset().getCount();
            showDataResults();
        }
    }

    private void showDatasetResults(){
        int i;
        for(i = 0; i < dataSetCount;i++) {
            System.out.println("Name: " + dataSetResults.get(i).getName());
            System.out.println("Acronym: " + dataSetResults.get(i).getId());
        }
    }

    private void showLocationResults(){
        int i;
        int x;
        if(locationCount < locationLimit){
            x = locationCount;
        }else {
            x = locationLimit;
        }
        for(i = 0;i< x;i++){
            System.out.println("Location Names: "+ locationResults.get(i).getName());
        }
    }

    private void setupData(){
        String startDate = "2017-01-01";
        String endDate = "2017-01-31";
        this.dataUuid = api.getData("GSOM",startDate,endDate);
    }
    private void showDataResults(){
        Log.v(TAG,"DATA RESULTS");
        int x;
        int i;
        if(dataLimit < dataCount){
            x = dataLimit;
        }else {
            x = dataCount;
        }
        for(i = 0;i<x;i++){
            int value = dataResults.get(i).getValue();
            String type = dataResults.get(i).getDatatype();
            System.out.println("Type: "+ type);
            System.out.println("Value: " + value);
        }

    }

}
