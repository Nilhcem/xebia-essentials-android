package com.nilhcem.xebia.essentials.dao;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.nilhcem.xebia.essentials.model.Card;

public class CardDao extends AbstractDao<Card> {
	public CardDao(Class<Card> dataClass) throws SQLException {
		super(dataClass);
	}

	public CardDao(ConnectionSource connectionSource, Class<Card> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
	}

	public CardDao(ConnectionSource connectionSource,
			DatabaseTableConfig<Card> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}
}
