package com.nilhcem.xebia.essentials.core;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.settings.SettingsActivity_;

public abstract class BaseActivity extends SherlockFragmentActivity {
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
}
