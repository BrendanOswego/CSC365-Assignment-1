package com.example.brendan.mainpackage.view;

/**
 * Created by brendan on 3/29/17.
 */

public class CityItem {

    String name;
    Double temp;
    Double prcp;

    public CityItem(String name, Double temp,Double prcp) {
        this.name = name;
        this.temp = temp;
        this.prcp = prcp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getPrcp() {
        return prcp;
    }

    public void setPrcp(Double prcp) {
        this.prcp = prcp;
    }
}
