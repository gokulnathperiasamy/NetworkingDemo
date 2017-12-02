package com.kpgn.weathermap.utility;

import com.kpgn.weathermap.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TextUtilTest {

    @Before
    public void setup() {

    }

    @Test
    public void getFormattedString_shouldFormatString() throws Exception {
        assertThat(TextUtil.getFormattedString(2233.343242)).isEqualTo("2233.3");
    }

}
