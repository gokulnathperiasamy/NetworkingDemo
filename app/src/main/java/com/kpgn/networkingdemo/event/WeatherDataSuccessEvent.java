package com.kpgn.networkingdemo.event;

import com.kpgn.networkingdemo.entity.WeatherData;

public class WeatherDataSuccessEvent {

    private WeatherData weatherData;

    public WeatherDataSuccessEvent(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}