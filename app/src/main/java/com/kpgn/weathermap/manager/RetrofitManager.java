package com.kpgn.weathermap.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class RetrofitManager {

    public static Retrofit getInstance(String URL, Context context) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(OkHttpManager.getInstance(context))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
