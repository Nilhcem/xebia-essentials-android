package com.nilhcem.xebia.essentials.search;

import java.sql.SQLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EProvider;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;
import com.nilhcem.xebia.essentials.core.dao.CardDao;
import com.nilhcem.xebia.essentials.core.model.Card;

@SuppressLint("Registered")
@EProvider
public class SearchSuggestionProvider extends ContentProvider {
	private static final String TAG = "SearchProvider";

	private static final String[] COLUMNS = new String[] {
		BaseColumns._ID,
		SearchManager.SUGGEST_COLUMN_TEXT_1,
		SearchManager.SUGGEST_COLUMN_INTENT_DATA
	};

	private CardDao mCardDao;

	@Override
	public boolean onCreate() {
		initDao();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String query = uri.getLastPathSegment();
		Log.d(TAG, "searching: " + query);

		MatrixCursor mc = null;
		List<Card> cards = mCardDao.getCardsFromSearchQuery(query);
		if (cards != null) {
			mc = new MatrixCursor(COLUMNS, cards.size());
			for (Card card : cards) {
				mc.addRow(new Object[] {
					card.getId(),
					card.getTitle(),
					card.getId()
				});
			}
		}
		return mc;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	private void initDao() {
		ConnectionSource source = OpenHelperManager.getHelper(getContext(),
				DatabaseHelper.class).getConnectionSource();
		try {
			mCardDao = DaoManager.createDao(source, Card.class);
		} catch (SQLException e) {
			Log.e(TAG, "Could not create DAO", e);
		}
	}
}
