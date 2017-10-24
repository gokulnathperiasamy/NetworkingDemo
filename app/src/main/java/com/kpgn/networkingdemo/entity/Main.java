package com.kpgn.networkingdemo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class Main {

    public double temp;
    public double pressure;
    public double humidity;

    @SerializedName("temp_min")
    public double tempMin;

    @SerializedName("temp_max")
    public double tempMax;

}
