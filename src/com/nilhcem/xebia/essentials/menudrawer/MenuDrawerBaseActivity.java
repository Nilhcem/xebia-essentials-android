package com.nilhcem.xebia.essentials.menudrawer;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.BaseActivity;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.qrcode.QRCodeScanner;

@EActivity
public abstract class MenuDrawerBaseActivity extends BaseActivity {
	private static final int DRAWER_MAX_SIZE_DP = 320;
	private static final int DRAWER_MARGIN_DP = 10;

	private MenuDrawer mMenuDrawer;
	private ListView mCategoriesListView;

	@Bean
	protected MenuDrawerListAdapter mCategoriesListAdapter;

	@Bean
	protected QRCodeScanner mCardScanner;

	@Override
	public void setContentView(int layoutResID) {
		// This override is only needed when using MENU_DRAG_CONTENT.
		mMenuDrawer.setContentView(layoutResID);
		onContentChanged();
	}

	@AfterInject
	protected void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			onMenuDrawerClosedWithNoAction();
			return;
		}
		super.onBackPressed();
	}

	@AfterInject
	protected void initMenuDrawer() {
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setMenuView(R.layout.menudrawer);
		mMenuDrawer.setOffsetMenuEnabled(false);

		// Set drawer's size
		int marginSize = Math.round(Compatibility.convertDpToPixel(DRAWER_MARGIN_DP, this));
		int maxDrawerSize = Math.round(Compatibility.convertDpToPixel(DRAWER_MAX_SIZE_DP, this));
		int screenWidth = Compatibility.getScreenDimensions(this).x;
		if (screenWidth > (maxDrawerSize + marginSize)) {
			mMenuDrawer.setMenuSize(maxDrawerSize);
		}
	}

	@AfterViews
	protected void initMenuDrawerList() {
		mCategoriesListView = (ListView) findViewById(R.id.menudrawerCategoriesList);
		mCategoriesListView.setAdapter(mCategoriesListAdapter);

		mCategoriesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				boolean categorySelected = false;
				if (id == Category.CATEGORY_ID_SCAN) {
					mCardScanner.initiateScan(MenuDrawerBaseActivity.this);
				} else if (id == Category.CATEGORY_ID_SEARCH) {
					onSearchRequested();
				} else {
					onMenuDrawerItemSelected(id);
					refreshMenuDrawer();
					categorySelected = true;
				}
				mMenuDrawer.closeMenu(true);
				if (!categorySelected) {
					onMenuDrawerClosedWithNoAction();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mCardScanner.onActivityResult(this, requestCode, resultCode, intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mMenuDrawer.toggleMenu();
			if (isMenuDrawerClosedOrClosing()) {
				onMenuDrawerClosedWithNoAction();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected boolean isMenuDrawerClosedOrClosing() {
		final int drawerState = mMenuDrawer.getDrawerState();
		return (drawerState == MenuDrawer.STATE_CLOSED || drawerState == MenuDrawer.STATE_CLOSING);
	}

	protected void refreshMenuDrawer() {
		mCategoriesListView.invalidateViews();
	}

	protected abstract void onMenuDrawerItemSelected(long id);

	protected abstract void onMenuDrawerClosedWithNoAction();
}
