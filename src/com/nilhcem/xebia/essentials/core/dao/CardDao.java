package com.nilhcem.xebia.essentials.core.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.text.TextUtils;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.nilhcem.xebia.essentials.core.model.Card;

public final class CardDao extends AbstractDao<Card> {
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

	public Card getByUrl(String url) {
		Card card = null;

		if (!TextUtils.isEmpty(url)) {
			// Fix issue with 2 cards (URL from the QR code is different from the real URL)
			if (url.equalsIgnoreCase("uncluttered-build")) {
				url = "clean-build";
			} else if (url.equalsIgnoreCase("honour-the-timebox")) {
				url = "honor-the-timebox";
			}

			QueryBuilder<Card, Long> queryBuilder = queryBuilder();
			try {
				queryBuilder.where().eq(Card.COL_URL, url);
				PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
				List<Card> cards = query(preparedQuery);
				if (cards.size() == 1) {
					card = cards.get(0);
				}
			} catch (SQLException e) {
				LOG.error("Error getting card from URL: {}", url, e);
			}
		}
		return card;
	}

	public Card getByBitly(String bitly) {
		Card card = null;

		if (!TextUtils.isEmpty(bitly)) {
			QueryBuilder<Card, Long> queryBuilder = queryBuilder();
			try {
				queryBuilder.where().eq(Card.COL_BITLY, bitly);
				PreparedQuery<Card> preparedQuery = queryBuilder.prepare();
				List<Card> cards = query(preparedQuery);
				if (cards.size() == 1) {
					card = cards.get(0);
				}
			} catch (SQLException e) {
				LOG.error("Error getting card from bitly: {}", bitly, e);
			}
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

	public List<Card> getCardsFromSearchQuery(String searchQuery) {
		List<Card> cards = new ArrayList<Card>();
		if (searchQuery != null) {
			searchQuery = searchQuery.trim().replaceAll("'", "''");
			try {
				GenericRawResults<Card> rawResults = queryRaw("select * from " + Card.TABLE_NAME + " where " + Card.COL_TITLE + " like '%" + searchQuery + "%'", getRawRowMapper());
				for (Card card : rawResults) {
					cards.add(card);
				}
			} catch (SQLException e) {
				LOG.error("Error getting cards from search query {}", searchQuery, e);
			}
		}
		return cards;
	}
}
