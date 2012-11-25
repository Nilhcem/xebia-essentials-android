package com.nilhcem.xebia.essentials.core;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public final class Compatibility {

	/**
	 * Checks if current SDK is compatible with the desired API level.
	 *
	 * @param apiLevel the required API level.
	 * @return {@code true} if current OS is compatible.
	 */
	public static boolean isCompatible(int apiLevel) {
		return android.os.Build.VERSION.SDK_INT >= apiLevel;
	}

	/**
	 * Sets a background drawable to a view.
	 */
	@SuppressWarnings("deprecation")
	public static void setDrawableToView(View view, Drawable drawable) {
		boolean oldWay = true;
		if (Compatibility.isCompatible(16)) {
			try {
				Method method = view.getClass().getDeclaredMethod(
						"setBackground", new Class[] { Drawable.class });
				method.invoke(view, drawable);
				oldWay = false;
			} catch (Exception e) {
				oldWay = true;
			}
		}
		if (oldWay) {
			view.setBackgroundDrawable(drawable);
		}
	}

	@SuppressWarnings("deprecation")
	public static Point getScreenDimensions(Context context) {
		boolean oldWay = true;
		Point size = new Point();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Compatibility.isCompatible(13)) {
			try {
				Method method = Display.class.getDeclaredMethod("getSize",
						new Class[] { Point.class });
				method.invoke(display, size);
				oldWay = false;
			} catch (Exception e) {
				oldWay = true;
			}
		}
		if (oldWay) {
			size.set(display.getWidth(), display.getHeight());
		}
		return size;
	}

	/**
	 * This method converts dp unit to equivalent device specific value in pixels.
	 *
	 * @param dp A value in dp (Device independent pixels) unit. Which we need to convert into pixels.
	 * @param context Context to get resources and device specific display metrics.
	 * @return A float value to represent Pixels equivalent to dp according to device.
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to device independent pixels.
	 *
	 * @param px A value in px (pixels) unit. Which we need to convert into dp.
	 * @param context Context to get resources and device specific display metrics.
	 * @return A float value to represent dp equivalent to px value.
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;

	}
}
