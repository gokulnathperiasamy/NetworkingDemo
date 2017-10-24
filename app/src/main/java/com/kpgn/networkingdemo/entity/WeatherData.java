package com.kpgn.networkingdemo.entity;

import java.util.List;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class WeatherData {

    public Coord coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public double visibility;
    public Wind wind;
    public Clouds clouds;
    public double dt;

}
