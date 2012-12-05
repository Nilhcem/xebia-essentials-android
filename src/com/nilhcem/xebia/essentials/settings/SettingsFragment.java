package com.nilhcem.xebia.essentials.settings;

import com.nilhcem.xebia.essentials.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public final class SettingsFragment extends PreferenceFragment {
	private ISettingsClickListenerInitializer mClickInitializer;

	private static final String KEY_CAT_GENERAL = "settings_cat_general";
	private static final String KEY_CAT_OTHER = "settings_cat_other";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int res = getActivity().getResources().getIdentifier(
				getArguments().getString("resource"), "xml",
				getActivity().getPackageName());
		addPreferencesFromResource(res);
		if (mClickInitializer != null) {
			mClickInitializer.initSettingsClickListeners(this);
		}
		setCategoriesTitles();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mClickInitializer = (ISettingsClickListenerInitializer) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mClickInitializer = null;
	}

	private void setCategoriesTitles() {
		PreferenceCategory catGeneral = (PreferenceCategory) findPreference(SettingsFragment.KEY_CAT_GENERAL);
		if (catGeneral != null) {
			catGeneral.setTitle(R.string.menu_settings);
		}

		PreferenceCategory catOther = (PreferenceCategory) findPreference(SettingsFragment.KEY_CAT_OTHER);
		if (catOther != null) {
			catOther.setTitle(R.string.menu_settings);
		}
	}
}
