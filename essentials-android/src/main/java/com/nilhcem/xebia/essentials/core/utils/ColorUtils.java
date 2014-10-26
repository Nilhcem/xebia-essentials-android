package com.nilhcem.xebia.essentials.core.utils;

import android.graphics.Color;

public final class ColorUtils {

    private ColorUtils() {
        throw new UnsupportedOperationException();
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
