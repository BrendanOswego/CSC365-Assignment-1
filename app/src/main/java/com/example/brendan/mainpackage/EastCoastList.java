package com.example.brendan.mainpackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for holding the list of East Coast States, instead of dumping it in onCreateView of MainFragment.
 */

public class EastCoastList {

    private List<String> list_eastCoastNames;

    /**
     * Public constructor, initializing new ArrayList adding all States to the ArrayList
     */
    public EastCoastList() {
        list_eastCoastNames = new ArrayList<>();
        list_eastCoastNames.add("Maine");
        list_eastCoastNames.add("New Hampshire");
        list_eastCoastNames.add("Massachusetts");
        list_eastCoastNames.add("Rhode Island");
        list_eastCoastNames.add("Connecticut");
        list_eastCoastNames.add("New York");
        list_eastCoastNames.add("New Jersey");
        list_eastCoastNames.add("Delaware");
        list_eastCoastNames.add("Maryland");
        list_eastCoastNames.add("Virginia");
        list_eastCoastNames.add("North Carolina");
        list_eastCoastNames.add("South Carolina");
        list_eastCoastNames.add("Georgia");
        list_eastCoastNames.add("Florida");
    }

    /**
     *
     * @return the class variable ArrayList
     */
    public List<String> getList() {
        return this.list_eastCoastNames;
    }

    /**
     *
     * @return size of ArrayList
     */
    public int size() {
        return 14;
    }
}
