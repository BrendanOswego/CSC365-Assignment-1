package com.example.brendan.mainpackage;

import java.util.List;

/**
 * Created by brendan on 1/27/17.
 */

public class LocationModel {

    private Metadata metadata;
    private List<Result> results = null;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
