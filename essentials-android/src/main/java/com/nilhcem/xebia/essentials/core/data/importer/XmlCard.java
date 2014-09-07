package com.nilhcem.xebia.essentials.core.data.importer;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "card")
public class XmlCard {

    @Attribute(name = "deprecated", required = false)
    boolean mDeprecated;

    @Element(name = "category")
    int mCategoryId;

    @Element(name = "title")
    String mTitle;

    @Element(name = "url")
    String mUrl;

    @Element(name = "summary", data = true)
    String mSummary;

    @Element(name = "description", data = true)
    String mDescription;

    @Element(name = "bitly", required = false)
    String mBitly;

    public int getCategoryId() {
        return mCategoryId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isDeprecated() {
        return mDeprecated;
    }

    public String getUrlId() {
        return mUrl;
    }

    public String getBitlyId() {
        return mBitly;
    }
}
