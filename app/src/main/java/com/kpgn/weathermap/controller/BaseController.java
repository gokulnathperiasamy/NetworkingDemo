package com.kpgn.weathermap.controller;

import com.kpgn.weathermap.constant.EndPoint;
import com.kpgn.weathermap.manager.OttoBusManager;
import com.kpgn.weathermap.manager.RetrofitManager;
import com.squareup.otto.Bus;

import retrofit2.Retrofit;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class BaseController {

    protected Bus bus = OttoBusManager.getInstance();
    protected Retrofit retrofit = RetrofitManager.getInstance(EndPoint.BASE_URL);

}
