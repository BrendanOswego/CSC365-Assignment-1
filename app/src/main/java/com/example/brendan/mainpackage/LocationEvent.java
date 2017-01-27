package com.example.brendan.mainpackage;

/**
 * Created by brendan on 1/27/17.
 */

public class LocationEvent {

    private LocationModel model;

    public LocationEvent(LocationModel model){
        this.model = model;
    }
    private LocationModel getLocation(){
        return this.model;
    }
}
