package com.nilhcem.xebia.essentials.core.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.graphics.Color;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nilhcem.xebia.essentials.core.dao.CategoryDao;

@Root
@DatabaseTable(daoClass = CategoryDao.class, tableName = Category.TABLE_NAME)
public final class Category {
	/*package*/ static final String TABLE_NAME = "categories";
	private static final String COL_ID = "_id";
	private static final String COL_NAME = "name";
	private static final String COL_COLOR = "color";

	@Attribute
	@DatabaseField(columnName = Category.COL_ID, id = true)
	private long id;

	@Element
	@DatabaseField(columnName = Category.COL_NAME)
	private String name;

	@Element
	@DatabaseField(columnName = Category.COL_COLOR)
	private String color;

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
		return Color.parseColor(getColor());
	}
}
