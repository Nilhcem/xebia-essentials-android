package com.nilhcem.xebia.essentials;

import java.sql.SQLException;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.cards.list.*;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.bo.CategoryService;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.core.model.XmlData;

@EActivity(R.layout.splash_screen)
public class SplashScreenActivity extends Activity {
	private static final Logger LOG = LoggerFactory.getLogger(SplashScreenActivity.class);
	private static final String XML_FILE = "data.xml";

	@ViewById(R.id.splashscreenLoading)
	protected LinearLayout mLoadingLayout;

	@Bean
	protected CategoryService mCategoryService;

	@Bean
	protected CardService mCardService;

	@Bean
	protected InMemoryCache mCache;

	@StringRes(R.string.splash_error)
	protected String mErrorMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			mCategoryService.getDao().insertAll(xml.getCategories());
			mCardService.getDao().insertAll(xml.getCards());
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
			databaseInitialized = (mCardService.getDao().countOf() > 0);
		} catch (SQLException e) {
			// Do nothing
		}
		return databaseInitialized;
	}

	@Background
	protected void initCategoriesThenRedirect() {
		try {
			List<Category> categories = mCategoryService.getDao().queryForAll();
			mCache.initCategories(categories);
		} catch (SQLException e) {
			LOG.error("Error getting categories", e);
			finishWithToastError(e.getMessage());
		}
		processRedirect();
	}

	private void processRedirect() {
		// Check intent filters (ie: http://essentials.xebia.com/have-fun)
		Card card = null;
		Uri data = getIntent().getData();
		if (data != null) {
			List<String> params = data.getPathSegments();
			if (params != null && params.size() == 1) {
				String cardUrl = params.get(0);
				card = mCardService.getDao().getByUrl(cardUrl);
			}
		}

		// Start activity
		Intent intent;
		if (card == null) {
			LOG.debug("Redirect to main activity");
			intent = new Intent(this, CardsListActivity_.class);
		} else {
			LOG.debug("Redirect to card activity");
			intent = new Intent(this, CardsHtmlActivity_.class);
			intent.putExtra(CardsHtmlActivity.INTENT_ITEM_ID, card.getId());
			intent.putExtra(CardsHtmlActivity.INTENT_DISPLAY_TYPE, CardsHtmlActivity.DISPLAY_ONE_CARD);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
