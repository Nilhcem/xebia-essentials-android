package com.nilhcem.xebia.essentials.core.data.importer;

import com.google.gson.annotations.SerializedName;

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
}
