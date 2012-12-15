package com.nilhcem.xebia.essentials.core.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.nilhcem.xebia.essentials.core.model.Card;

public final class CardDao extends AbstractDao<Card> {
	private static final String TAG = "CardDao";

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
			Log.e(TAG, "Error getting card " + cardId, e);
			card = null;
		}
		return card;
	}

	public Card getByUrl(String cardUrl) {
		Card card = null;

		if (!TextUtils.isEmpty(cardUrl)) {
			// Fix issue with 2 cards (URL from the QR code is different from the real URL)
			String url = null;
			if (cardUrl.equalsIgnoreCase("uncluttered-build")) {
				url = "clean-build";
			} else if (cardUrl.equalsIgnoreCase("honour-the-timebox")) {
				url = "honor-the-timebox";
			} else {
				url = cardUrl;
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
				Log.e(TAG, "Error getting card from URL: " + url, e);
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
				Log.e(TAG, "Error getting card from bitly: " + bitly, e);
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
			Log.e(TAG, "Error getting cards from category " + catId, e);
		}
		return null;
	}

	public List<Card> getCardsFromSearchQuery(String searchQuery) {
		List<Card> cards = new ArrayList<Card>();
		if (searchQuery != null) {
			String searchTerm = searchQuery.trim().replaceAll("'", "''");
			try {
				// Results that matches the title should be displayed first
				String query = String.format(Locale.US, "select * from %s where %s like '%%%s%%'",
						Card.TABLE_NAME, Card.COL_TITLE, searchTerm);
				GenericRawResults<Card> rawResults = queryRaw(query, getRawRowMapper());
				for (Card card : rawResults) {
					cards.add(card);
				}

				// If no match, get results that match the summary, or the HTML description
				if (cards.size() == 0) {
					query = String.format(Locale.US, "select * from %s where (%s like '%%%s%%' OR %s like '%%%s%%')",
									Card.TABLE_NAME, Card.COL_SUMMARY, searchTerm, Card.COL_CONTENT, searchTerm);

					rawResults = queryRaw(query, getRawRowMapper());
					for (Card card : rawResults) {
						if (!cards.contains(card)) {
							cards.add(card);
						}
					}
				}
			} catch (SQLException e) {
				Log.e(TAG, "Error getting cards from search query " + searchTerm, e);
			}
		}
		return cards;
	}
}
