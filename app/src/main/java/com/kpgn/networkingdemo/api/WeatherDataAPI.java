package com.kpgn.networkingdemo.api;

import com.kpgn.networkingdemo.entity.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherDataAPI {

    @GET("current.json")
    Call<WeatherData> getWeatherData(@Query("key") String apiKey, @Query("q") String cityName);
}