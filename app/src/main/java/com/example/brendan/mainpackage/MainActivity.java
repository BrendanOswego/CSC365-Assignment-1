package com.example.brendan.mainpackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.DataSetEvent;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.model.ResultDataSet;
import com.example.brendan.mainpackage.model.Resultset;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    APIClass api;
    List<Result> locationResults;
    List<ResultDataSet> dataResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        api = new APIClass();

        //api.getLocations();
        api.getDataSets();

    }
    @Subscribe
    public void onLocationEvent(LocationEvent event){
        this.locationResults = event.getLocation().getResults();
    }
    @Subscribe
    public void onDataSetEvent(DataSetEvent event){
        this.dataResults = event.getDataSet().getResults();
    }


}
