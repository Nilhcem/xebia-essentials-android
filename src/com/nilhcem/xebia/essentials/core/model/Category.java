package com.nilhcem.xebia.essentials.core.model;

import java.util.Locale;

import android.graphics.Color;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nilhcem.xebia.essentials.core.dao.CategoryDao;

@DatabaseTable(daoClass = CategoryDao.class, tableName = Category.TABLE_NAME)
public final class Category {
	public static final long CATEGORY_ID_ALL = 0;
	public static final long CATEGORY_ID_SCAN = -1;
	public static final long CATEGORY_ID_SEARCH = -2;

	public static final String TABLE_NAME = "categories";
	public static final String COL_ID = "_id";
	public static final String COL_NAME = "name";
	public static final String COL_COLOR = "color";

	@DatabaseField(columnName = Category.COL_ID, id = true)
	private long id;

	@DatabaseField(columnName = Category.COL_NAME)
	private String name;

	@DatabaseField(columnName = Category.COL_COLOR)
	private String color;

	public Category() {
	}

	public Category(long id, String name, String color) {
		initCategory(id, name, color);
	}

	public Category(long id, String name, int color) {
		initCategory(id, name, String.format(Locale.US, "#%x", color));
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return color;
	}

	public int getIntColor() {
		int intColor;
		try {
			intColor = Color.parseColor(getColor());
		} catch (IllegalArgumentException e) {
			intColor = 0;
		}
		return intColor;
	}

	private void initCategory(long id, String name, String color) {
		this.id = id;
		this.name = name;
		this.color = color;
	}
}
