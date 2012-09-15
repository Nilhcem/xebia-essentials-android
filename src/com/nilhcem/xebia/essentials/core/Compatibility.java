package com.nilhcem.xebia.essentials.core;

import java.lang.reflect.Method;

import android.graphics.drawable.Drawable;
import android.view.View;

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
}
