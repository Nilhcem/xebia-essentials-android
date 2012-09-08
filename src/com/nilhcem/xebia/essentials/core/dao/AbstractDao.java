package com.nilhcem.xebia.essentials.core.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public abstract class AbstractDao<T> extends BaseDaoImpl<T, Long> implements Dao<T, Long> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDao.class);

	public AbstractDao(Class<T> dataClass) throws SQLException {
		super(dataClass);
	}

	public AbstractDao(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	public AbstractDao(ConnectionSource connectionSource,
			DatabaseTableConfig<T> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	public void insertAll(final List<T> objs) {
		try {
			TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (T obj : objs) {
						AbstractDao.this.createOrUpdate(obj);
					}
					return null;
				}
			});
		} catch (SQLException e) {
			LOG.error("Error inserting objects in DB", e);
		}
	}
}
