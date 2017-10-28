package com.kpgn.networkingdemo.controller;

import com.kpgn.networkingdemo.api.WeatherDataAPI;
import com.kpgn.networkingdemo.entity.WeatherData;
import com.kpgn.networkingdemo.event.WeatherDataFailureEvent;
import com.kpgn.networkingdemo.event.WeatherDataSuccessEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataController extends BaseController implements Callback<WeatherData> {

    public void getWeatherData(String apiKey, String cityName) {
        WeatherDataAPI weatherDataAPI = retrofit.create(WeatherDataAPI.class);

        Call<WeatherData> call = weatherDataAPI.getWeatherData(apiKey, cityName);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
        if (response.isSuccessful()) {
            try {
                WeatherData weatherData = response.body();
                bus.post(new WeatherDataSuccessEvent(weatherData));
            } catch (Exception e) {
                fail();
            }
        } else {
            fail();
        }
    }

    @Override
    public void onFailure(Call<WeatherData> call, Throwable t) {
        fail();
    }

    protected void fail() {
        bus.post(new WeatherDataFailureEvent());
    }
}