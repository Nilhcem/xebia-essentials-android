package com.nilhcem.xebia.essentials;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.cards.list.*;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.dao.CardDao;
import com.nilhcem.xebia.essentials.core.dao.CategoryDao;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.qrcode.QRCodeScanner;
import com.nilhcem.xebia.essentials.xml.XmlData;
import com.nilhcem.xebia.essentials.xml.XmlParser;

@EActivity(R.layout.splash_screen)
public class SplashScreenActivity extends Activity {
	private static final String TAG = "SplashScreenActivity";
	private static final String XML_FILE = "data.xml";

	@ViewById(R.id.splashscreenLoading)
	protected LinearLayout mLoadingLayout;

	@StringRes(R.string.splash_error)
	protected String mErrorMessage;

	@StringRes(R.string.intent_no_card_found)
	protected String mNoCardFound;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Category.class)
	protected CategoryDao mCategoryDao;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Card.class)
	protected CardDao mCardDao;

	@Bean
	protected InMemoryCache mCache;

	@Bean
	protected QRCodeScanner mQRCodeScanner;

	@Bean
	protected XmlParser mXmlParser;

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isDbInitialized()) {
			initCategoriesInCacheThenRedirect();
		} else {
			mLoadingLayout.setVisibility(View.VISIBLE);
			importData();
		}
	}

	@Background
	protected void importData() {
		Log.d(TAG, "Importing XML data...");

		InputStream xmlStream = null;
		try {
			xmlStream = getAssets().open(SplashScreenActivity.XML_FILE);
			XmlData xml = mXmlParser.parseXml(xmlStream);
			mCategoryDao.insertAll(xml.getCategories());
			mCardDao.insertAll(xml.getCards());
			initCategoriesInCacheThenRedirect();
		} catch (Exception e) {
			Log.e(TAG, "Error importing data", e);
			finishWithToastError(String.format(Locale.getDefault(), mErrorMessage, e.getMessage()));
		} finally {
			if (xmlStream != null) {
				try {
					xmlStream.close();
				} catch (IOException e) {
					Log.e(TAG, "Cannot close stream", e);
				}
			}
		}
	}

	@UiThread
	protected void finishWithToastError(String errorMsg) {
		Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
		finish();
	}

	@Background
	protected void initCategoriesInCacheThenRedirect() {
		try {
			List<Category> categories = mCategoryDao.queryForAll();
			mCache.initCategories(categories);
		} catch (SQLException e) {
			Log.e(TAG, "Error getting categories", e);
			finishWithToastError(String.format(Locale.getDefault(), mErrorMessage, e.getMessage()));
		}
		processRedirect();
	}

	private void processRedirect() {
		// Check for intent filters (search - qrcode), if any
		List<Card> cards = getCardsFromIntent();

		// Start activity
		Intent startActivityIntent = null;
		if (cards == null) {
			Log.d(TAG, "No specific intent - redirect to main activity");
			startActivityIntent = createMainActivityRedirectIntent(null, Category.CATEGORY_ID_ALL);
		} else {
			int cardsSize = cards.size();

			if (cardsSize == 0) {
				Log.d(TAG, "No card found - finish activity");
				finishWithToastError(mNoCardFound);
				return ;
			} else if (cardsSize == 1) {
				Log.d(TAG, "One card found - redirect to card activity");
				startActivityIntent = mQRCodeScanner.createIntent(this, cards.get(0));
			} else {
				Log.d(TAG, "Multiple cards found - redirect to main activity");
				startActivityIntent = createMainActivityRedirectIntent(cards, Category.CATEGORY_ID_SEARCH);
			}
		}
		startActivity(startActivityIntent);
	}

	private List<Card> getCardsFromIntent() {
		List<Card> cards = null;
		Intent intent = getIntent();

		if (intent != null) {
			String action = intent.getAction();

			if (action != null) {
				// Search suggestion or external QR code scanner
				if (action.equals(Intent.ACTION_VIEW)) {
					Uri data = intent.getData();
					if (data != null) {
						List<String> params = data.getPathSegments();
						if (params != null && params.size() == 1) {
							String cardUrl = params.get(0);
							Card card = mCardDao.getByUrl(cardUrl);
							if (card != null) {
								cards = new ArrayList<Card>();
								cards.add(card);
							}
						}
					}
				// Search result
				} else if (action.equals(Intent.ACTION_SEARCH)) {
					String searchQuery = intent.getStringExtra(SearchManager.QUERY);
					cards = mCardDao.getCardsFromSearchQuery(searchQuery);
				}
			}
		}
		return cards;
	}

	private Intent createMainActivityRedirectIntent(List<Card> cards, long selectedCategory) {
		mCache.setSelectedCategory(selectedCategory, cards);
		Intent intent = new Intent(this, CardsListActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	private boolean isDbInitialized() {
		boolean databaseInitialized = false;

		try {
			databaseInitialized = (mCardDao.countOf() > 0);
		} catch (SQLException e) {
			// Do nothing
		}
		return databaseInitialized;
	}
}
