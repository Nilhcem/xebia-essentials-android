package com.nilhcem.xebia.essentials.core;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public final class Compatibility {

	/**
	 * Checks if current SDK is compatible with the desired API level.
	 *
	 * @param apiLevel
	 *            the required API level.
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
}
