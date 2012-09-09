package com.nilhcem.xebia.essentials.core.dao;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.nilhcem.xebia.essentials.core.model.Card;

public class CardDao extends AbstractDao<Card> {
	protected static final Logger LOG = LoggerFactory.getLogger(CardDao.class);

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

	public Card getById(Long cardId) {
		Card card;
		try {
			card = queryForId(cardId);
		} catch (SQLException e) {
			LOG.error("Error getting card {}", cardId, e);
			card = null;
		}
		return card;
	}

	public List<Card> getAllCardsFromCategory(long catId) {
		QueryBuilder<Card, Long> queryBuilder = queryBuilder();

		try {
			if (catId > 0) {
				queryBuilder.where().eq(Card.COL_CATEGORY, catId);
			}
			PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
			return query(preparedQuery);
		} catch (SQLException e) {
			LOG.error("Error getting cards from category {}", catId, e);
		}
		return null;
	}
}
