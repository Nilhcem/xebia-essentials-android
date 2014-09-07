package com.nilhcem.xebia.essentials.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public final class Compatibility {

    private Compatibility() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if the current SDK is compatible with the desired API level.
     *
     * @param apiLevel the required API level
     * @return {@code true} if current OS is compatible
     */
    public static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }

    /**
     * Sets a background drawable to a view.
     */
    @TargetApi(JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (Compatibility.isCompatible(JELLY_BEAN)) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Converts dp unit to equivalent device specific value in pixels.
     *
     * @param dp      A value in dp (Device independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return a float value to represent Pixels equivalent to dp according to device
     */
    public static float convertDpToPixel(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int convertDpToIntPixel(float dp, Context context) {
        return Math.round(convertDpToPixel(dp, context));
    }

    /**
     * Unregisters the specified listener from the viewTreeObserver.
     *
     * @param viewTreeObserver the viewTreeObserver from which to remove the listener
     * @param listener         the listener to remove
     */
    @TargetApi(JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Compatibility.isCompatible(JELLY_BEAN)) {
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(listener);
        }
    }
}