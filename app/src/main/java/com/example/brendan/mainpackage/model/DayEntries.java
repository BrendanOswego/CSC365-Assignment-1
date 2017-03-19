package com.example.brendan.mainpackage.model;

import java.util.ArrayList;

/**
 * Created by Brendan on 3/15/2017.
 */

public class DayEntries {

    ArrayList<DataEntries> data;
    String date;

    public ArrayList<DataEntries> getDataEntries() {
        return data;
    }

    public void setDataEntries(ArrayList<DataEntries> data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
