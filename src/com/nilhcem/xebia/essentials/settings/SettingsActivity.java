package com.nilhcem.xebia.essentials.settings;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.nilhcem.xebia.essentials.R;

@EActivity
public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {
	private static final String GITHUB_URL = "https://github.com/Nilhcem/xebia-essentials-android";
	private static final String KEY_ABOUT_XEBIA = "xebia";
	private static final String KEY_ABOUT_CARDS = "cards";
	private static final String KEY_LICENSES = "licenses";
	private static final String KEY_GITHUB = "github";

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

	@Override
	protected void onPause() {
		super.onPause();
		dismissIfNotNull(mAboutXebiaDialog);
		dismissIfNotNull(mAboutCardsDialog);
		dismissIfNotNull(mLicensesDialog);
	}

	private void dismissIfNotNull(Dialog dialog) {
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
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SettingsActivity.GITHUB_URL)));
			return true;
		}
		return false;
	}

	private void createDialogs() {
		mAboutXebiaDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_XEBIA);
		mAboutCardsDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_CARDS);
		mLicensesDialog = new AboutCustomDialog(this, AboutCustomDialogView.DIALOG_LICENSES);
	}
}
