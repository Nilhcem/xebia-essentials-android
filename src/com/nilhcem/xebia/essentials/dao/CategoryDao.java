package com.nilhcem.xebia.essentials.dao;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.nilhcem.xebia.essentials.model.Category;

public class CategoryDao extends AbstractDao<Category> {
	public CategoryDao(Class<Category> dataClass) throws SQLException {
		super(dataClass);
	}

	public CategoryDao(ConnectionSource connectionSource,
			Class<Category> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public CategoryDao(ConnectionSource connectionSource,
			DatabaseTableConfig<Category> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}
}
