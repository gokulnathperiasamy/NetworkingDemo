package com.kpgn.weathermap.utility;

import com.kpgn.weathermap.entity.CityData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gokulnathkp on 18/11/17.
 */

public class CityDataHelper {

    private static List<CityData> cityDataList;

    private static void initCityDataList() {
        cityDataList = new ArrayList<>();

        cityDataList.add(getDefaultCity());

        CityData cityDataChennai = new CityData();
        cityDataChennai.lat = 13.0827;
        cityDataChennai.lon = 80.2707;
        cityDataChennai.cityName = "Chennai";
        cityDataList.add(cityDataChennai);

        CityData cityDataMumbai = new CityData();
        cityDataMumbai.lat = 19.0760;
        cityDataMumbai.lon = 72.8777;
        cityDataMumbai.cityName = "Mumbai";
        cityDataList.add(cityDataMumbai);
    }

    public static List<CityData> getCityDataList() {
        initCityDataList();
        return cityDataList;
    }

    public static CityData getDefaultCity() {
        CityData cityDataBangalore = new CityData();
        cityDataBangalore.lat = 12.9716;
        cityDataBangalore.lon = 77.5946;
        cityDataBangalore.cityName = "Bangalore";

        return cityDataBangalore;
    }

    public static String getSelectedMarkerLatLon(String markerTitle) {
        if (cityDataList == null) {
            initCityDataList();
        }
        for (CityData cityData : cityDataList) {
            if (markerTitle.equals(cityData.getString())) {
                return cityData.getLatLonString();
            }
        }
        return getDefaultCity().getLatLonString();
    }

}
