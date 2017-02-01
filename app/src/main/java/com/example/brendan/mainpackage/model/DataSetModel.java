package com.example.brendan.mainpackage.model;

import java.util.List;

/**
 * Created by brendan on 1/30/17.
 */

public class DataSetModel {

    private Metadata metadata;
    private List<ResultDataSet> results = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<ResultDataSet> getResults() {
        return results;
    }

    public void setResults(List<ResultDataSet> results) {
        this.results = results;
    }
}
