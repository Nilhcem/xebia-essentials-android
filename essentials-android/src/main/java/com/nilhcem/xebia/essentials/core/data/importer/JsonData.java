package com.nilhcem.xebia.essentials.core.data.importer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonData {

    @SerializedName("categories")
    List<JsonCategory> mCategories;

    @SerializedName("cards")
    List<JsonCard> mCards;

    public List<JsonCategory> getCategories() {
        return mCategories;
    }

    public List<JsonCard> getCards() {
        return mCards;
    }
}
