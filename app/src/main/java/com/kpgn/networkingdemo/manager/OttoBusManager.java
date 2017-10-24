package com.kpgn.networkingdemo.manager;

import com.squareup.otto.Bus;

/**
 * Created by gokulnathkp on 24/10/17.
 */

public class OttoBusManager {

    private static Bus bus;

    private OttoBusManager() {

    }

    public static Bus getInstance() {
        if (bus == null) {
            synchronized (OttoBusManager.class) {
                if (bus == null) {
                    bus = new Bus();
                }
            }
        }
        return bus;
    }

}
