package com.example.brendan.mainpackage;

/**
 * Created by brendan on 1/27/17.
 */

public class Result {

    private String mindate;
    private String maxdate;
    private String name;
    private Float datacoverage;
    private String id;

    public String getMindate() {
        return mindate;
    }

    public void setMindate(String mindate) {
        this.mindate = mindate;
    }

    public String getMaxdate() {
        return maxdate;
    }

    public void setMaxdate(String maxdate) {
        this.maxdate = maxdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDatacoverage() {
        return datacoverage;
    }

    public void setDatacoverage(Float datacoverage) {
        this.datacoverage = datacoverage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
