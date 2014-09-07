package com.nilhcem.xebia.essentials.test.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class Compatibility {

    private Compatibility() {
        throw new UnsupportedOperationException();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Activity activity) {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        if (com.nilhcem.xebia.essentials.core.utils.Compatibility.isCompatible(Build.VERSION_CODES.HONEYCOMB_MR2)) {
            Point referencePoint = new Point();
            defaultDisplay.getSize(referencePoint);
            return referencePoint;
        } else {
            return new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        }
    }
}
