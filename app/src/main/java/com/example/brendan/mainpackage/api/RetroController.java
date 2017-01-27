package com.example.brendan.mainpackage.api;

/**
 * Created by brendan on 1/27/17.
 */

public class RetroController {

    private final static String base_url = "http://www.ncdc.noaa.gov/cdo-web/api/v2/";

    public static RetroInterface getServer(){
        return RetroClient.getClient(base_url).create(RetroInterface.class);
    }

}
