package com.example.brendan.mainpackage.api;

/**
 * Singleton class for accessing the API that contains the base URL for all API calls
 */

class RetroController {

    private final static String base_url = "https://www.ncdc.noaa.gov/cdo-web/api/v2/";

    static RetroInterface getServer(){
        return RetroClient.getClient(base_url).create(RetroInterface.class);
    }

}
