package com.nilhcem.xebia.essentials.settings;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
	private static final Logger LOG = LoggerFactory.getLogger(SettingsActivity.class);

	private static final String GITHUB_URL = "https://github.com/Nilhcem/xebia-essentials-android";
	private static final String LICENSES_FILE = "LICENSES";
	private static final String KEY_LICENSES = "licenses";
	private static final String KEY_GITHUB = "github";

	private AlertDialog mLicensesDialog = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		findPreference(SettingsActivity.KEY_LICENSES).setOnPreferenceClickListener(this);
		findPreference(SettingsActivity.KEY_GITHUB).setOnPreferenceClickListener(this);
		createLicensesDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mLicensesDialog != null) {
			mLicensesDialog.dismiss();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();

		if (key.equals(SettingsActivity.KEY_LICENSES)) {
			mLicensesDialog.show();
			return true;
		} else if (key.equals(SettingsActivity.KEY_GITHUB)) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SettingsActivity.GITHUB_URL)));
			return true;
		}

		return false;
	}

	private void createLicensesDialog() {
		String licenses = getLicensesFromAssets();
		mLicensesDialog = new Builder(this)
				.setMessage(licenses)
				.setCancelable(true)
				.setPositiveButton(getString(R.string.settings_close),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						}).create();
	}

	private String getLicensesFromAssets() {
		String licenses;

		try {
			InputStream stream = getAssets().open(SettingsActivity.LICENSES_FILE);
			licenses = IOUtils.toString(stream);
		} catch (IOException e) {
			LOG.error("Error while reading {} file", SettingsActivity.LICENSES_FILE, e);
			licenses = "";
		}
		return licenses;
	}
}
