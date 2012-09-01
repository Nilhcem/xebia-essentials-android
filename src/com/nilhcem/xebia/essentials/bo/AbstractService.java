package com.nilhcem.xebia.essentials.bo;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;

public abstract class AbstractService<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);
	protected Dao<T, Long> mDao;

	public Dao<T, Long> getDao() {
		return mDao;
	}

	protected void setDao(Context context, Class<T> clazz) {
		try {
			mDao = DaoManager.createDao(
					OpenHelperManager.getHelper(context, DatabaseHelper.class)
							.getConnectionSource(), clazz);
		} catch (SQLException e) {
			LOG.error("Error creating DAO", e);
		}
	}
}
