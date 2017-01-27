package com.example.brendan.mainpackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.brendan.mainpackage.api.APIClass;
import com.example.brendan.mainpackage.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    APIClass api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        api = new APIClass();

        api.getLocations();


    }
    @Subscribe
    public void onEvent(LocationEvent event){
        for(int i =0;i< event.getLocation().getMetadata().getResultset().getLimit();i++) {
            System.out.println("Results: " + event.getLocation().getResults().get(i).getName());
        }
    }
}
