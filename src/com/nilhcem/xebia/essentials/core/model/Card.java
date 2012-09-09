package com.nilhcem.xebia.essentials.core.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nilhcem.xebia.essentials.core.dao.CardDao;

@Root
@DatabaseTable(daoClass = CardDao.class, tableName = Card.TABLE_NAME)
public final class Card {
	public static final String TABLE_NAME = "cards";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_QRCODE = "qrcode";
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

	@Element(name = "qrcode")
	@DatabaseField(columnName = Card.COL_QRCODE)
	private String qrCode;

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

	public String getQrCode() {
		return qrCode;
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
}
