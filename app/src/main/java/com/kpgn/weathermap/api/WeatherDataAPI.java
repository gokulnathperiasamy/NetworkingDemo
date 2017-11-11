package com.kpgn.weathermap.api;

import com.kpgn.weathermap.entity.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherDataAPI {

    @GET("current.json")
    Call<WeatherData> getWeatherData(@Query("key") String apiKey, @Query("q") String cityName);
}