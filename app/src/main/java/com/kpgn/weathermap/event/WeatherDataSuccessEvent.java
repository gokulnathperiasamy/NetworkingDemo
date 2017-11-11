package com.kpgn.weathermap.event;

import com.kpgn.weathermap.entity.WeatherData;

public class WeatherDataSuccessEvent {

    public WeatherData weatherData;

    public WeatherDataSuccessEvent(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}