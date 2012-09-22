package com.nilhcem.xebia.essentials.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.github.eddieringle.android.libs.undergarment.widgets.DrawerGarment;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.BaseActivity;

@EActivity
public abstract class DashboardBaseActivity extends BaseActivity {
	private static final String EXTRA_SHOW_DASH = "DashboardBaseActivity:showDashboard";

	private ActionBar mActionBar;
	private DrawerGarment mDrawerGarment;
	private ListView mDashboardListView;

	@Bean
	protected DashboardListAdapter mDashboardListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			if (savedInstanceState.getBoolean(EXTRA_SHOW_DASH, false)) {
				mDrawerGarment.openDrawer(false);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(EXTRA_SHOW_DASH, mDrawerGarment.isDrawerOpened());
	}

	@AfterInject
	protected void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerGarment.isDrawerOpened()) {
			mDrawerGarment.closeDrawer(true);
		} else {
			super.onBackPressed();
		}
	}

	@AfterInject
	protected void initDashboard() {
		mDrawerGarment = new DrawerGarment(this, R.layout.dashboard);
		mDrawerGarment.setSlideTarget(DrawerGarment.SLIDE_TARGET_CONTENT);
		mDrawerGarment.setDrawerCallbacks(new DrawerGarment.IDrawerCallbacks() {
			@Override
			public void onDrawerOpened() {
				mActionBar.setDisplayHomeAsUpEnabled(false);
			}

			@Override
			public void onDrawerClosed() {
				mActionBar.setDisplayHomeAsUpEnabled(true);
			}
		});
	}

	@AfterViews
	protected void initDashboardList() {
		mDashboardListView = (ListView) findViewById(R.id.dashboardList);
		mDashboardListView.setAdapter(mDashboardListAdapter);
		mDashboardListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onDashboardItemSelected(id);
				mDrawerGarment.closeDrawer(true);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerGarment.isDrawerOpened()) {
				onBackPressed();
			} else {
				mDrawerGarment.openDrawer(true);
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	protected abstract void onDashboardItemSelected(long id);
}
