package com.nilhcem.xebia.essentials.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.nilhcem.xebia.essentials.*;
import com.nilhcem.xebia.essentials.settings.*;

@EActivity
public abstract class BaseActivity extends SherlockFragmentActivity {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseActivity.class);

	@Bean
	protected InMemoryCache mCache;

	@Override
	protected void onResume() {
		super.onResume();
		checkCacheInitialization();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.main_menu_settings) {
			startActivity(new Intent(this, SettingsActivity_.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Stops activity and redirects to Splash Screen if not initialized.
	 */
	private void checkCacheInitialization() {
		if (!mCache.isInitialized()) {
			LOGGER.warn("Cache is not initialized, redirect to SplashScreenActivity");
			Intent intent = new Intent(this, SplashScreenActivity_.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);
		}
	}
}
