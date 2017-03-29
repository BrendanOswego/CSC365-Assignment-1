package com.example.brendan.mainpackage.model;

import java.util.List;

/**
 * Created by brendan on 3/24/17.
 */

public class DayModel {

    List<KVModel> data;
    String date;

    public List<KVModel> getData() {
        return data;
    }

    public void setData(List<KVModel> data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
