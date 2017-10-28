package com.kpgn.networkingdemo.event;

import com.kpgn.networkingdemo.entity.WeatherData;

public class WeatherDataSuccessEvent {

    public WeatherData weatherData;

    public WeatherDataSuccessEvent(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}