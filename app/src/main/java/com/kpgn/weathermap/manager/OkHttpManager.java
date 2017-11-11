package com.kpgn.weathermap.manager;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class OkHttpManager {

    private static OkHttpClient okHttpClient;

    private OkHttpManager() {

    }

    public static OkHttpClient getInstance(Context context) {
        if (okHttpClient == null) {
            synchronized (OkHttpManager.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new ChuckInterceptor(context))
                            .build();
                }
            }
        }
        return okHttpClient;
    }

}
