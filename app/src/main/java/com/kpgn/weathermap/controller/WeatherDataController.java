package com.kpgn.weathermap.controller;

import android.content.Context;

import com.kpgn.weathermap.api.WeatherDataAPI;
import com.kpgn.weathermap.constant.EndPoint;
import com.kpgn.weathermap.entity.WeatherData;
import com.kpgn.weathermap.event.WeatherDataFailureEvent;
import com.kpgn.weathermap.event.WeatherDataSuccessEvent;
import com.kpgn.weathermap.manager.RetrofitManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataController extends BaseController implements Callback<WeatherData> {

    public void getWeatherData(String apiKey, String cityName, Context context) {
        retrofit = RetrofitManager.getInstance(EndPoint.BASE_URL, context);
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

    private void fail() {
        bus.post(new WeatherDataFailureEvent());
    }
}