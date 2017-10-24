package com.kpgn.networkingdemo.controller;

import com.kpgn.networkingdemo.constant.EndPoint;
import com.kpgn.networkingdemo.manager.OttoBusManager;
import com.kpgn.networkingdemo.manager.RetrofitManager;
import com.squareup.otto.Bus;

import retrofit2.Retrofit;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class BaseController {

    protected Bus bus = OttoBusManager.getInstance();
    protected Retrofit retrofit = RetrofitManager.getInstance(EndPoint.BASE_URL);

}
