package com.example.brendan.mainpackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendan on 2/8/17.
 */

public class EastCoastList {

    List<String> list_eastCoastNames = new ArrayList<>();

    public EastCoastList(){
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


    public List<String> getList(){
        return this.list_eastCoastNames;
    }

    public int size(){
        return 14;
    }
}
