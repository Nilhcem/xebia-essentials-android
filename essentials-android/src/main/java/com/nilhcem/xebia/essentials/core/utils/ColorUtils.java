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

    public static int mixColors(int color1, int color2, float factor) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - factor;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * factor) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * factor) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * factor) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * factor) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }
}
