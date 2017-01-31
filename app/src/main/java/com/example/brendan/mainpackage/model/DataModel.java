package com.example.brendan.mainpackage.model;

import java.util.List;

/**
 * Created by brendan on 1/31/17.
 */

public class DataModel {

    private Metadata metadata;
    private List<DataResults> results = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<DataResults> getResults() {
        return results;
    }

    public void setResults(List<DataResults> results) {
        this.results = results;
    }
}
