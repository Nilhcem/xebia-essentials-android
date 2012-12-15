package com.nilhcem.xebia.essentials.core;

import android.content.Intent;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.nilhcem.xebia.essentials.*;
import com.nilhcem.xebia.essentials.settings.*;

@EActivity
@OptionsMenu(R.menu.main_menu)
public abstract class BaseActivity extends SherlockFragmentActivity {
	private static final String TAG = "BaseActivity";

	@Bean
	protected InMemoryCache mCache;

	@BooleanRes(R.bool.multipaned)
	protected boolean mIsMultipaned;

	@Override
	protected void onResume() {
		super.onResume();
		checkCacheInitialization();
	}

	@OptionsItem(R.id.main_menu_settings)
	protected void menuSettingsSelected() {
		startActivity(new Intent(this, SettingsActivity_.class));
	}

	/**
	 * Stops activity and redirects to Splash Screen if not initialized.
	 */
	private void checkCacheInitialization() {
		if (!mCache.isInitialized()) {
			Log.w(TAG, "Cache is not initialized, redirect to SplashScreenActivity");
			Intent intent = new Intent(this, SplashScreenActivity_.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);
			overridePendingTransition(0,0);
		}
	}
}
