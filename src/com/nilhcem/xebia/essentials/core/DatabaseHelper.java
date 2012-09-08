package com.nilhcem.xebia.essentials.core;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

public final class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseHelper.class);
	private static final String DATABASE_NAME = "xebiacards.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Card.class);
		} catch (SQLException e) {
			LOG.error("Error creating DB tables", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
	}
}
