package com.nilhcem.xebia.essentials.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;

@EActivity
public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {
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
		addPreferencesFromResource(R.xml.settings);
		findPreference(SettingsActivity.KEY_ABOUT_XEBIA).setOnPreferenceClickListener(this);
		findPreference(SettingsActivity.KEY_ABOUT_CARDS).setOnPreferenceClickListener(this);
		findPreference(SettingsActivity.KEY_LICENSES).setOnPreferenceClickListener(this);
		findPreference(SettingsActivity.KEY_GITHUB).setOnPreferenceClickListener(this);
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

	public static int getViewMode() {
		int viewMode = (sViewMode == null) ? VIEW_MODE_CARD : sViewMode;
		return viewMode;
	}

	public static synchronized void switchViewMode(Activity activity) {
		SharedPreferences prefs = activity.getPreferences(MODE_PRIVATE);
		if (sViewMode == null) {
			sViewMode = prefs.getInt(KEY_VIEW_MODE, VIEW_MODE_CARD);
		}

		int newViewMode = (sViewMode == VIEW_MODE_CARD) ? VIEW_MODE_DETAILS : VIEW_MODE_CARD;

		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(KEY_VIEW_MODE, newViewMode);
		prefsEditor.commit();

		sViewMode = newViewMode;
	}
}
