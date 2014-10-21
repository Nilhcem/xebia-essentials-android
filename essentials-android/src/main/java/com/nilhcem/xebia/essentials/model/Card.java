package com.nilhcem.xebia.essentials.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.nilhcem.xebia.essentials.core.data.importer.JsonCard;

@Table(name = "cards")
public class Card extends Model implements Parcelable {

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Column(name = "is_deprecated")
    boolean mDeprecated;

    @Column(name = "title", index = true)
    String mTitle;

    @Column(name = "url")
    String mUrl;

    @Column(name = "summary")
    String mSummary;

    @Column(name = "content")
    String mDescription;

    @Column(name = "bitly")
    String mBitly;

    @Column(name = "category")
    Category mCategory;

    public Card() {
        super();
    }

    public Card(JsonCard jsonCard, Category category) {
        super();
        mTitle = jsonCard.getTitle();
        mUrl = jsonCard.getUrl();
        mSummary = jsonCard.getSummary();
        mDescription = jsonCard.getDescription();
        mBitly = jsonCard.getBitly();
        mDeprecated = jsonCard.isDeprecated();
        mCategory = category;
    }

    private Card(Parcel in) {
        super();
        mDeprecated = in.readByte() != 0;
        mTitle = in.readString();
        mUrl = in.readString();
        mSummary = in.readString();
        mDescription = in.readString();
        mBitly = in.readString();
        mCategory = in.readParcelable(Category.class.getClassLoader());
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

    public Category getCategory() {
        return mCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(mDeprecated ? (byte) 1 : (byte) 0);
        dest.writeString(mTitle);
        dest.writeString(mUrl);
        dest.writeString(mSummary);
        dest.writeString(mDescription);
        dest.writeString(mBitly);
        dest.writeParcelable(mCategory, 0);
    }
}
