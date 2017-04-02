package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.LocationModel;

import java.util.UUID;

/**
 * Event class for handling Location API calls
 */

public class LocationEvent {

    private LocationModel model;
    private UUID uuid;

    public LocationEvent(UUID uuid,LocationModel model){
        this.model = model;
        this.uuid = uuid;
    }

    public LocationEvent() {
    }

    public LocationModel getLocation(){
        return this.model;
    }

    public UUID getUuid(){
        return this.uuid;
    }
}
