package com.example.brendan.mainpackage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.LocationEvent;
import com.example.brendan.mainpackage.model.DataResults;
import com.example.brendan.mainpackage.model.Result;
import com.example.brendan.mainpackage.model.ResultDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.UUID;

/**
 * Created by brendan on 2/1/17.
 */

public class MainFragment extends BaseFragment {

    private APIClass api;
    private List<Result> locationResults;
    private List<ResultDataSet> dataSetResults;
    private List<DataResults> dataResults;
    private int dataSetCount;
    private int locationCount;
    private int dataLimit;
    private int dataCount;
    private String firstId;
    private UUID locationUuid;
    private UUID datasetUuid;
    private UUID dataUuid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new APIClass();
        EventBus.getDefault().register(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main,container,false);
        locationUuid = api.getLocations();
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event){
        int i,j;
        if(event.getUuid().equals(this.locationUuid)){
            locationResults = event.getLocation().getResults();
            i = event.getLocation().getMetadata().getResultset().getCount();
            j = event.getLocation().getMetadata().getResultset().getLimit();
            if(i < j){
                locationCount = i;
            }else {
                locationCount = j;
            }
        }
    }

    private void locationEventInfo(){
        int i;
        for(i = 0;i<locationCount;i++){
            if(locationResults.get(i) != null){
                String name = locationResults.get(i).getName();
                String id = locationResults.get(i).getId();
                System.out.println("Name: " + name);
                System.out.println("ID: " + id);
            }
        }
    }

}
