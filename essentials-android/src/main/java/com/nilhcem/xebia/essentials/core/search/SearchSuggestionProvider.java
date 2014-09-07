package com.nilhcem.xebia.essentials.core.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.EssentialsModule;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

public class SearchSuggestionProvider extends ContentProvider {

    private static final String[] COLUMNS = new String[]{
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA
    };

    @Inject CardsDao mCardsDao;

    @Override
    public boolean onCreate() {
        EssentialsApplication app = (EssentialsApplication) getContext().getApplicationContext();
        ObjectGraph.create(new EssentialsModule(app)).inject(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment();
        Timber.d("Searching: %s", query);

        MatrixCursor mc = null;
        List<Card> cards = mCardsDao.getCardsFromSearchQuery(query);
        if (!cards.isEmpty()) {
            mc = new MatrixCursor(COLUMNS, cards.size());
            for (Card card : cards) {
                mc.addRow(new Object[]{
                        card.getId(),
                        card.getTitle(),
                        card.getUrlId()
                });
            }
        }
        return mc;
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
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
