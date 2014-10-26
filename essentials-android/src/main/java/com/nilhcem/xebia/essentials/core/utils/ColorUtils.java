package com.nilhcem.xebia.essentials.core.utils;

import android.graphics.Color;

import timber.log.Timber;

public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException();
    }

    public static int parseColor(String color) {
        int intColor;
        try {
            intColor = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            Timber.w(e, "Error parsing color %s", color);
            intColor = 0;
        }
        return intColor;
    }

    public static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }
}
