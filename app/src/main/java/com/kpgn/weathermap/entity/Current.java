package com.kpgn.weathermap.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class Current {
    @SerializedName("last_updated")
    public String lastUpdated;

    @SerializedName("temp_c")
    public double tempInC;

    @SerializedName("temp_f")
    public double tempInF;

    public Condition condition;

    @SerializedName("feelslike_c")
    public double feelsLikeInC;

    @SerializedName("feelslike_f")
    public double feelsLikeInF;
}
