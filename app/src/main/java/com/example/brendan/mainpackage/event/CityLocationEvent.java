package com.example.brendan.mainpackage.event;

import com.example.brendan.mainpackage.model.LocationModel;

import java.util.UUID;

/**
 * Created by brendan on 3/29/17.
 */

public class CityLocationEvent {

    private UUID uuid;
    private LocationModel model;
    private String url;

    public CityLocationEvent(UUID uuid, LocationModel model, String url) {
        this.uuid = uuid;
        this.model = model;
        this.url = url;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocationModel getModel() {
        return model;
    }

    public void setModel(LocationModel model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
