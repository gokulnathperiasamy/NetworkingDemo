package com.kpgn.networkingdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.networkingdemo.R;
import com.kpgn.networkingdemo.constant.ApplicationConstant;
import com.kpgn.networkingdemo.constant.CityName;
import com.kpgn.networkingdemo.controller.WeatherDataController;
import com.kpgn.networkingdemo.event.WeatherDataFailureEvent;
import com.kpgn.networkingdemo.event.WeatherDataSuccessEvent;
import com.kpgn.networkingdemo.manager.ConnectionManager;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
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

    @BindView(R.id.iv_condition)
    ImageView mCondition;

    @BindView(R.id.tv_cloudy)
    TextView mCloudy;

    @BindView(R.id.tv_temp_c)
    TextView mTempInC;

    @BindView(R.id.tv_temp_f)
    TextView mTempInF;

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
        if (ConnectionManager.isNetworkAvailable(this)) {
            mAVILoadingIndicator.setVisibility(View.VISIBLE);
            mAVILoadingIndicator.show();
            mErrorContainer.setVisibility(View.GONE);
            mWeatherDataContainer.setVisibility(View.GONE);

            // Wait for 2 seconds!
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    new WeatherDataController().getWeatherData(ApplicationConstant.API_KEY, CityName.CITY_NAME_BANGALORE);
                }
            }, 2000);
        } else {
            updateUI(null, false);
        }
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
            mCloudy.setText(successEvent.weatherData.current.condition.text);
            mTempInC.setText(getString(R.string.temperature_string_f, successEvent.weatherData.current.tempInF));
            mTempInF.setText(getString(R.string.temperature_string_c, successEvent.weatherData.current.tempInC));
            Picasso.with(this).load("http:" + successEvent.weatherData.current.condition.icon).into(mCondition);
        } else {
            mWeatherDataContainer.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
        }
    }
}
