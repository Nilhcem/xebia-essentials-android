package com.nilhcem.xebia.essentials.core.data.importer;

import com.google.gson.annotations.SerializedName;

public class JsonCard {

    @SerializedName("deprecated")
    boolean mDeprecated;

    @SerializedName("category")
    int mCategory;

    @SerializedName("title")
    String mTitle;

    @SerializedName("url")
    String mUrl;

    @SerializedName("summary")
    String mSummary;

    @SerializedName("description")
    String mDescription;

    @SerializedName("bitly")
    String mBitly;

    public boolean isDeprecated() {
        return mDeprecated;
    }

    public int getCategory() {
        return mCategory;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getBitly() {
        return mBitly;
    }
}
