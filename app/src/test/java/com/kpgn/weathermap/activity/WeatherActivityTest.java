package com.kpgn.weathermap.activity;

import com.kpgn.weathermap.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class WeatherActivityTest {

    WeatherActivity weatherActivity;
    ActivityController<WeatherActivity> activityController;


    @Before
    public void setup() {
        weatherActivity = new WeatherActivity();
        activityController = ActivityController.of(Robolectric.getShadowsAdapter(), weatherActivity);
        activityController.setup();
    }

    @Test
    public void onCreate_shouldSetTitle() throws Exception {
        assertThat(weatherActivity.getTitle()).isEqualTo("Weather Map");
    }

}
