package com.nilhcem.xebia.essentials.settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.Compatibility;

@EActivity
public class SettingsActivity extends SherlockPreferenceActivity implements
		OnPreferenceClickListener, ISettingsClickListenerInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(SettingsActivity.class);

	public static final String KEY_ANIMATION = "cards_animation";
	public static final String KEY_VIEW_MODE = "viewMode"; // see VIEW_MODE_*
	public static final int VIEW_MODE_CARD = 1;
	public static final int VIEW_MODE_DETAILS = 2;

	private static final String KEY_ABOUT_XEBIA = "xebia";
	private static final String KEY_ABOUT_CARDS = "cards";
	private static final String KEY_LICENSES = "licenses";
	private static final String KEY_GITHUB = "github";

	private static Integer sViewMode = null; // see VIEW_MODE_*

	@StringRes(R.string.github_url)
	protected String mGithubUrl;

	private Dialog mLicensesDialog = null;
	private Dialog mAboutXebiaDialog = null;
	private Dialog mAboutCardsDialog = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!Compatibility.isCompatible(Build.VERSION_CODES.HONEYCOMB)) {
			addPreferencesFromResource(R.xml.settings_general);
			addPreferencesFromResource(R.xml.settings_other);
			initSettingsClickListeners(this);
		}
		createDialogs();
	}

	private void createDialogs() {
		mAboutXebiaDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_XEBIA);
		mAboutCardsDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_CARDS);
		mLicensesDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_LICENSES);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dismissDialogIfNotNull(mAboutXebiaDialog);
		dismissDialogIfNotNull(mAboutCardsDialog);
		dismissDialogIfNotNull(mLicensesDialog);
	}

	private void dismissDialogIfNotNull(Dialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();

		if (key.equals(SettingsActivity.KEY_ABOUT_XEBIA)) {
			mAboutXebiaDialog.show();
			return true;
		} else if (key.equals(SettingsActivity.KEY_ABOUT_CARDS)) {
			mAboutCardsDialog.show();
			return true;
		} else if (key.equals(SettingsActivity.KEY_LICENSES)) {
			mLicensesDialog.show();
			return true;
		} else if (key.equals(SettingsActivity.KEY_GITHUB)) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGithubUrl)));
			return true;
		}
		return false;
	}

	public static int getViewMode(Activity activity) {
		if (activity != null && sViewMode == null) {
			SharedPreferences prefs = activity.getPreferences(MODE_PRIVATE);
			if (sViewMode == null) {
				sViewMode = prefs.getInt(KEY_VIEW_MODE, VIEW_MODE_CARD);
			}
		}
		return sViewMode;
	}

	public static synchronized void switchViewMode(Activity activity) {
		int viewMode = getViewMode(activity);
		int newViewMode = (viewMode == VIEW_MODE_CARD) ? VIEW_MODE_DETAILS : VIEW_MODE_CARD;

		SharedPreferences prefs = activity.getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(KEY_VIEW_MODE, newViewMode);
		prefsEditor.commit();

		sViewMode = newViewMode;
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		try {
			Method method = getClass().getMethod("loadHeadersFromResource", int.class, List.class);
			method.invoke(this, new Object[] { R.xml.settings_headers, target });
		} catch (NoSuchMethodException e) {
			// Do nothing
		} catch (IllegalArgumentException e) {
			// Do nothing
		} catch (IllegalAccessException e) {
			// Do nothing
		} catch (InvocationTargetException e) {
			// Do nothing
		}
	}

	@Override
	public void initSettingsClickListeners(Object object) {
		final String[] keys = new String[] {
			SettingsActivity.KEY_ABOUT_XEBIA,
			SettingsActivity.KEY_ABOUT_CARDS,
			SettingsActivity.KEY_LICENSES,
			SettingsActivity.KEY_GITHUB
		};

		try {
			Method method = object.getClass().getMethod("findPreference", new Class[] { CharSequence.class });
			for (String key : keys) {
				Preference pref = (Preference) method.invoke(object, key);
				if (pref != null) {
					pref.setOnPreferenceClickListener(this);
				}
			}
		} catch (NoSuchMethodException e) {
			LOG.warn("", e);
		} catch (IllegalArgumentException e) {
			LOG.warn("", e);
		} catch (IllegalAccessException e) {
			LOG.warn("", e);
		} catch (InvocationTargetException e) {
			LOG.warn("", e);
		}
	}
}
