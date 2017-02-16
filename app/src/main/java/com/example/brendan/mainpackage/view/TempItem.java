package com.example.brendan.mainpackage.view;

/**
 * Model class for Temperature Items in MainFragment ListView
 */

public class TempItem {
    private String name;
    private String fips;
    private Double value;

    public TempItem(String name, String fips, Double value) {
        this.name = name;
        this.fips = fips;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFips() {
        return fips;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
