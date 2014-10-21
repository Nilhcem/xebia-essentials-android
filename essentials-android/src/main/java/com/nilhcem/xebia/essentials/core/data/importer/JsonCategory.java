package com.nilhcem.xebia.essentials.core.data.importer;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

public class JsonCategory {

    @SerializedName("id")
    int mId;

    @SerializedName("color")
    String mColor;

    @SerializedName("name")
    String mName;

    public int getId() {
        return mId;
    }

    public String getColor() {
        return mColor;
    }

    public String getName() {
        return mName;
    }

    public int getIntColor() {
        int intColor;
        try {
            intColor = Color.parseColor(mColor);
        } catch (IllegalArgumentException e) {
            Timber.w(e, "Error parsing color");
            intColor = 0;
        }
        return intColor;
    }
}
