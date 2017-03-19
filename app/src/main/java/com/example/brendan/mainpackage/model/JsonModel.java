package com.example.brendan.mainpackage.model;

import java.util.ArrayList;

/**
 * Created by Brendan on 3/15/2017.
 */

public class JsonModel {

    ArrayList<DayEntries> days;

    public ArrayList<DayEntries> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayEntries> days) {
        this.days = days;
    }
}
