package com.kpgn.weathermap.manager;

import android.content.Context;

import com.kpgn.weathermap.BuildConfig;
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
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    if (BuildConfig.DEBUG) {
                        builder.addInterceptor(new ChuckInterceptor(context));
                    }
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

}
