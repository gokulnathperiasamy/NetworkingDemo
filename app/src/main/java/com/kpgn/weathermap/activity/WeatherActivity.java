package com.kpgn.weathermap.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kpgn.weathermap.R;
import com.kpgn.weathermap.constant.ApplicationConstant;
import com.kpgn.weathermap.constant.CityName;
import com.kpgn.weathermap.controller.WeatherDataController;
import com.kpgn.weathermap.event.WeatherDataFailureEvent;
import com.kpgn.weathermap.event.WeatherDataSuccessEvent;
import com.kpgn.weathermap.manager.ConnectionManager;
import com.kpgn.weathermap.utility.TextUtil;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.avi_loading)
    AVLoadingIndicatorView mAVILoadingIndicator;

    @BindView(R.id.error_container)
    View mErrorContainer;

    @BindView(R.id.weather_data_container)
    View mWeatherDataContainer;

    @BindView(R.id.tv_weather_city)
    TextView mWeatherCity;

    @BindView(R.id.avi_condition)
    AVLoadingIndicatorView mAVICondition;

    @BindView(R.id.iv_condition)
    ImageView mCondition;

    @BindView(R.id.tv_weather_condition)
    TextView mWeatherCondition;

    @BindView(R.id.tv_temp_current)
    TextView mTempCurrent;

    @BindView(R.id.tv_temp_feels_like)
    TextView mTempFeelsLike;

    @BindView(R.id.tv_updated_on)
    TextView mUpdatedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        setupMapsAndMarker();
        loadWeatherData();
    }

    private void setupMapsAndMarker() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_marker);
        mapFragment.getMapAsync(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                loadWeatherData();
                break;
        }

        return super.onOptionsItemSelected(item);
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
                    new WeatherDataController().getWeatherData(ApplicationConstant.API_KEY,
                            CityName.CITY_NAME_BANGALORE, getApplicationContext());
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

            mWeatherCity.setText(getString(R.string.weather_header,
                    successEvent.weatherData.location.name,
                    successEvent.weatherData.location.region,
                    successEvent.weatherData.location.country));
            mWeatherCondition.setText(successEvent.weatherData.current.condition.text);

            mTempCurrent.setText(getString(R.string.temperature_string_current,
                    TextUtil.getFormattedString(successEvent.weatherData.current.tempInC),
                    TextUtil.getFormattedString(successEvent.weatherData.current.tempInF)));

            mTempFeelsLike.setText(getString(R.string.temperature_string_feels_like,
                    TextUtil.getFormattedString(successEvent.weatherData.current.feelsLikeInC),
                    TextUtil.getFormattedString(successEvent.weatherData.current.feelsLikeInF)));

            mUpdatedOn.setText(getString(R.string.updated_on_string,
                    successEvent.weatherData.current.lastUpdated));

            Picasso.with(this)
                    .load("http:" + successEvent.weatherData.current.condition.icon)
                    .into(mCondition, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            mCondition.setVisibility(View.VISIBLE);
                            mAVICondition.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mCondition.setVisibility(View.VISIBLE);
                            mCondition.setImageDrawable(getResources().getDrawable(R.drawable.ic_loading));
                        }
                    });
        } else {
            mWeatherDataContainer.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
