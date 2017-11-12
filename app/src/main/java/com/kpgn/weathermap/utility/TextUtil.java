package com.kpgn.weathermap.utility;

import java.util.Locale;

/**
 * Created by gokulnathkp on 12/11/17.
 */

public abstract class TextUtil {

    public static String getFormattedString(double doubleValue) {
        return String.format(Locale.US, "%.01f", doubleValue);
    }

}
