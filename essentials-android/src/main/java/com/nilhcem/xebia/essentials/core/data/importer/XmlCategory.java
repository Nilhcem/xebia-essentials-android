package com.nilhcem.xebia.essentials.core.data.importer;

import android.graphics.Color;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import timber.log.Timber;

@Root(name = "category")
public class XmlCategory {

    @Attribute(name = "id")
    int mId;

    @Attribute(name = "color")
    String mColor;

    @Text
    String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    private String getColor() {
        return mColor;
    }

    public int getIntColor() {
        int intColor;
        try {
            intColor = Color.parseColor(getColor());
        } catch (IllegalArgumentException e) {
            Timber.w(e, "Error parsing color");
            intColor = 0;
        }
        return intColor;
    }
}
