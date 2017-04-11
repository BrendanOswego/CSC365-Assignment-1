package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.DataModel;

import java.util.UUID;

/**
 * Created by brendan on 3/29/17.
 */

public class CityDataEvent {

    private DataModel model;
    private UUID uuid;
    private String url;

    public CityDataEvent(DataModel model, UUID uuid, String url) {
        this.model = model;
        this.uuid = uuid;
        this.url = url;
    }

    public DataModel getModel() {
        return model;
    }

    public void setMode(DataModel model) {
        this.model = model;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
