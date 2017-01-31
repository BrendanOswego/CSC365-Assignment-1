package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.DataModel;

import java.util.UUID;

/**
 * Created by brendan on 1/31/17.
 */

public class DataEvent {

    private DataModel model;
    private UUID uuid;

    public DataEvent(UUID uuid,DataModel model){
        this.uuid  = uuid;
        this.model = model;
    }

    public DataModel getDataModel(){return this.model;}

    public UUID getUuid(){ return this.uuid;}
}
