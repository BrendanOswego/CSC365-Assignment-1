package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.DataSetModel;

/**
 * Created by brendan on 1/30/17.
 */

public class DataSetEvent {

    private DataSetModel model;

    public DataSetEvent(DataSetModel model){
        this.model = model;
    }

    public DataSetModel getDataSet(){
        return this.model;
    }


}
