package com.kpgn.weathermap.entity;

/**
 * Created by gokulnathkp on 18/11/17.
 */

public class CityData {
    public double lat;
    public double lon;
    public String cityName;

    public String getString() {
        return cityName + " (" + lat + ", " + lon + ")";
    }

    public String getLatLonString() {
        return lat + ", " + lon;
    }
}
