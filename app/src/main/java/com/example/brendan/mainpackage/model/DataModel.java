package com.example.brendan.mainpackage.model;

import java.util.List;

/**
 * Model class for Data from JSON
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
