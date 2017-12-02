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
public class BaseActivityTest {

    BaseActivity baseActivity;
    ActivityController<BaseActivity> activityController;


    @Before
    public void setup() {
        baseActivity = new BaseActivity();
        activityController = ActivityController.of(Robolectric.getShadowsAdapter(), baseActivity);
        activityController.setup();
    }

    @Test
    public void onCreate_shouldInitOttoBus() throws Exception {
        assertThat(baseActivity.bus).isNotNull();
    }

}
