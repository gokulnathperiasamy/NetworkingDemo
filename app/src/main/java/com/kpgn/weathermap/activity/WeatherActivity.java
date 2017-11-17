package com.kpgn.weathermap.activity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.current_location_map);
        mapFragment.getMapAsync(this);

        loadWeatherData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
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
                    .placeholder(R.drawable.ic_loading)
                    .into(mCondition);
        } else {
            mWeatherDataContainer.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
