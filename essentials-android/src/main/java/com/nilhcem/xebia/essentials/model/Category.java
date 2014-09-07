package com.nilhcem.xebia.essentials.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.nilhcem.xebia.essentials.core.data.importer.XmlCategory;

public class Category extends Model implements Parcelable {

    public static final int CATEGORY_ID_ALL = 0;
    public static final int CATEGORY_ID_SCAN = -1;
    public static final int CATEGORY_ID_SEARCH = -2;
    public static final int CATEGORY_ID_ABOUT = -3;

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    private int mId;

    @Column(name = "color")
    int mColor;

    @Column(name = "name", index = true)
    String mName;

    public Category() {
        super();
    }

    public Category(XmlCategory xml) {
        super();
        mColor = xml.getIntColor();
        mName = xml.getName();
    }

    public Category(int id, String name, int color) {
        super();
        mId = id;
        mName = name;
        mColor = color;
    }

    private Category(Parcel in) {
        super();
        mId = in.readInt();
        mColor = in.readInt();
        mName = in.readString();
    }

    public int getIntId() {
        if (getId() != null) {
            return getId().intValue();
        }
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getColor() {
        return mColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getIntId());
        dest.writeInt(mColor);
        dest.writeString(mName);
    }
}
