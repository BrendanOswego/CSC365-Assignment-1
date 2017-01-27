package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.LocationModel;

/**
 * Created by brendan on 1/27/17.
 */

public class LocationEvent {

    private LocationModel model;

    public LocationEvent(LocationModel model){
        this.model = model;
    }

    public LocationModel getLocation(){
        return this.model;
    }
}
