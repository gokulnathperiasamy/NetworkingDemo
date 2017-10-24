package com.kpgn.networkingdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.kpgn.networkingdemo.R;
import com.kpgn.networkingdemo.constant.ApplicationConstant;
import com.kpgn.networkingdemo.constant.CityID;
import com.kpgn.networkingdemo.controller.WeatherDataController;
import com.kpgn.networkingdemo.event.WeatherDataFailureEvent;
import com.kpgn.networkingdemo.event.WeatherDataSuccessEvent;
import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.avi_loading)
    AVLoadingIndicatorView mAVILoadingIndicator;

    @BindView(R.id.error_container)
    View mErrorContainer;

    @BindView(R.id.weather_data_container)
    View mWeatherDataContainer;

    @BindView(R.id.tv_cloudy)
    TextView mCloudy;

    @BindView(R.id.tv_max_temperature)
    TextView mMaxTemp;

    @BindView(R.id.tv_min_temperature)
    TextView mMinTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        hideActionBar();
        loadWeatherData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void loadWeatherData() {
        mAVILoadingIndicator.setVisibility(View.VISIBLE);
        mAVILoadingIndicator.show();
        mErrorContainer.setVisibility(View.GONE);
        mWeatherDataContainer.setVisibility(View.GONE);

        // Wait for 2 seconds!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                new WeatherDataController().getWeatherData(CityID.CITY_ID_BANGALORE, ApplicationConstant.API_KEY);
            }
        }, 2000);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onWeatherDataSuccessEvent(WeatherDataSuccessEvent successEvent) {
        updateUI(successEvent, true);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onWeatherDataFailureEvent(WeatherDataFailureEvent failureEvent) {
        updateUI(null, false);
    }

    private void updateUI(WeatherDataSuccessEvent successEvent, boolean isSuccess) {
        mAVILoadingIndicator.hide();
        mAVILoadingIndicator.setVisibility(View.GONE);
        if (isSuccess && successEvent != null) {
            mWeatherDataContainer.setVisibility(View.VISIBLE);
            mErrorContainer.setVisibility(View.GONE);
            mCloudy.setText(successEvent.getWeatherData().weather.get(0).description);
            mMaxTemp.setText(getString(R.string.temperature_string, successEvent.getWeatherData().main.tempMax));
            mMinTemp.setText(getString(R.string.temperature_string, successEvent.getWeatherData().main.tempMin));
        } else {
            mWeatherDataContainer.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
        }
    }
}
