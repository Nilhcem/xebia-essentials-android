package com.nilhcem.xebia.essentials;

import java.sql.SQLException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.bo.CardService;
import com.nilhcem.xebia.essentials.bo.CategoryService;
import com.nilhcem.xebia.essentials.dao.CardDao;
import com.nilhcem.xebia.essentials.dao.CategoryDao;
import com.nilhcem.xebia.essentials.model.XmlData;

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

	@StringRes(R.string.splash_error)
	protected String mErrorMessage;

	@Override
	protected void onResume() {
		super.onResume();
		if (isDbInitialized()) {
			redirectToMainActivity();
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
			((CategoryDao) mCategoryService.getDao()).insertAll(xml.getCategories());
			((CardDao) mCardService.getDao()).insertAll(xml.getCards());
			redirectToMainActivity();
		} catch (Exception e) {
			LOG.error("Error importing data", e);
			displayError(e.getMessage());
			finish();
		}
	}

	@UiThread
	protected void displayError(String errorMsg) {
		Toast.makeText(this, String.format(mErrorMessage, errorMsg),
				Toast.LENGTH_SHORT).show();
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

	private void redirectToMainActivity() {
		LOG.debug("Redirect to main activity");
		Intent intent = new Intent(this, MainActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
