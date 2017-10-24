package com.kpgn.networkingdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kpgn.networkingdemo.manager.OttoBusManager;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {

    protected Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        bus = OttoBusManager.getInstance();
    }
}
