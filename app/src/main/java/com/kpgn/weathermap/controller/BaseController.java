package com.kpgn.weathermap.controller;

import com.kpgn.weathermap.manager.OttoBusManager;
import com.squareup.otto.Bus;

import retrofit2.Retrofit;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class BaseController {

    protected Bus bus = OttoBusManager.getInstance();
    protected Retrofit retrofit;

}
