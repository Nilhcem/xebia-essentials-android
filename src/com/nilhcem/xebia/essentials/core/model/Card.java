package com.nilhcem.xebia.essentials.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nilhcem.xebia.essentials.core.dao.CardDao;

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

	@DatabaseField(columnName = Card.COL_TITLE)
	private String title;

	@DatabaseField(columnName = Card.COL_CATEGORY)
	private long categoryId;

	@DatabaseField(columnName = Card.COL_BITLY)
	private String bitly;

	@DatabaseField(columnName = Card.COL_URL)
	private String url;

	@DatabaseField(columnName = Card.COL_SUMMARY)
	private String summary;

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

	public Card(String title, long categoryId, String bitly, String url, String summary, String content) {
		this.title = title;
		this.categoryId = categoryId;
		this.bitly = bitly;
		this.url = url;
		this.summary = summary;
		this.content = content;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Card other = (Card) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}
}
