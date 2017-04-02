package com.example.brendan.mainpackage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Data from JSON
 */

public class DataModel {

    private Metadata metadata;
    private ArrayList<DataResults> results = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ArrayList<DataResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<DataResults> results) {
        this.results = results;
    }
}
