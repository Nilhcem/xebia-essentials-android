package com.nilhcem.xebia.essentials;

import java.sql.SQLException;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.nilhcem.xebia.essentials.core.model.XmlData;
import com.nilhcem.xebia.essentials.scanner.CardScanner;

@EActivity(R.layout.splash_screen)
public class SplashScreenActivity extends Activity {
	private static final Logger LOG = LoggerFactory.getLogger(SplashScreenActivity.class);
	private static final String XML_FILE = "data.xml";

	@ViewById(R.id.splashscreenLoading)
	protected LinearLayout mLoadingLayout;

	@StringRes(R.string.splash_error)
	protected String mErrorMessage;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Category.class)
	protected CategoryDao mCategoryDao;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Card.class)
	protected CardDao mCardDao;

	@Bean
	protected InMemoryCache mCache;

	@Bean
	protected CardScanner mCardScanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mCache.setSelectedCategory(Category.ALL_CATEGORIES_ID);
		if (isDbInitialized()) {
			initCategoriesThenRedirect();
		} else {
			// Display loading - Import data - Redirect to main activity
			mLoadingLayout.setVisibility(View.VISIBLE);
			importData();
		}
	}

	@Background
	protected void importData() {
		LOG.debug("Importing XML data...");
		Serializer serializer = new Persister();
		try {
			XmlData xml = serializer.read(XmlData.class, getAssets().open(SplashScreenActivity.XML_FILE));
			mCategoryDao.insertAll(xml.getCategories());
			mCardDao.insertAll(xml.getCards());
			initCategoriesThenRedirect();
		} catch (Exception e) {
			LOG.error("Error importing data", e);
			finishWithToastError(e.getMessage());
		}
	}

	@UiThread
	protected void finishWithToastError(String errorMsg) {
		Toast.makeText(this, String.format(mErrorMessage, errorMsg),
				Toast.LENGTH_SHORT).show();
		finish();
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

	@Background
	protected void initCategoriesThenRedirect() {
		try {
			List<Category> categories = mCategoryDao.queryForAll();
			mCache.initCategories(categories);
		} catch (SQLException e) {
			LOG.error("Error getting categories", e);
			finishWithToastError(e.getMessage());
		}
		processRedirect();
	}

	private void processRedirect() {
		// Check intent filters if any (when a card is scanned from an external app)
		Card card = mCardScanner.checkIntentData(getIntent().getData());

		// Start activity
		Intent intent;
		if (card == null) {
			LOG.debug("Redirect to main activity");
			intent = new Intent(this, CardsListActivity_.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		} else {
			LOG.debug("Redirect to card activity");
			intent = mCardScanner.createIntent(this, card);
		}
		startActivity(intent);
	}
}
