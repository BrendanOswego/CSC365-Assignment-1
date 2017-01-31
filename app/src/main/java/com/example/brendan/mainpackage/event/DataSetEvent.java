package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.DataSetModel;

import java.util.UUID;

/**
 * Event class to handle DataSet API calls
 */

public class DataSetEvent {

    private DataSetModel model;
    private UUID uuid;
    public DataSetEvent(UUID uuid, DataSetModel model){
        this.model = model;
        this.uuid = uuid;
    }

    public DataSetModel getDataSet(){
        return this.model;
    }

    public UUID getUuid(){
        return this.uuid;
    }


}
