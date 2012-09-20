package com.nilhcem.xebia.essentials.core.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nilhcem.xebia.essentials.core.dao.CardDao;

@Root
@DatabaseTable(daoClass = CardDao.class, tableName = Card.TABLE_NAME)
public final class Card implements Parcelable {
	public static final String TABLE_NAME = "cards";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_BITLY = "bitly";
	public static final String COL_URL = "url";
	public static final String COL_SUMMARY = "summary";
	public static final String COL_CONTENT = "content";
	public static final String COL_CATEGORY = "category_id";

	@DatabaseField(columnName = Card.COL_ID, generatedId = true)
	private Long id;

	@Element
	@DatabaseField(columnName = Card.COL_TITLE)
	private String title;

	@Element(name = "category")
	@DatabaseField(columnName = Card.COL_CATEGORY)
	private long categoryId;

	@Element
	@DatabaseField(columnName = Card.COL_BITLY)
	private String bitly;

	@Element
	@DatabaseField(columnName = Card.COL_URL)
	private String url;

	@Element
	@DatabaseField(columnName = Card.COL_SUMMARY)
	private String summary;

	@Element
	@DatabaseField(columnName = Card.COL_CONTENT)
	private String content;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public String getBitly() {
		return bitly;
	}

	public String getUrl() {
		return url;
	}

	public String getSummary() {
		return summary;
	}

	public String getContent() {
		return content;
	}

	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
		@Override
		public Card createFromParcel(Parcel source) {
			return new Card(source);
		}

		@Override
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};

	public Card() {
	}

	private Card(Parcel in) {
		id = in.readLong();
		title = in.readString();
		categoryId = in.readLong();
		bitly = in.readString();
		url = in.readString();
		summary = in.readString();
		content = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeLong(categoryId);
		dest.writeString(bitly);
		dest.writeString(url);
		dest.writeString(summary);
		dest.writeString(content);
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
