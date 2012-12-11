package com.nilhcem.xebia.essentials.menudrawer;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import android.content.Intent;
import android.os.Bundle;
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
import com.nilhcem.xebia.essentials.core.*;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.qrcode.QRCodeScanner;

@EActivity
public abstract class MenuDrawerBaseActivity extends BaseActivity {
	private static final String EXTRA_STATE_MENUDRAWER = "MenuDrawerBaseActivity:stateMenuDrawer";

	private MenuDrawerManager mMenuDrawer;
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

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMenuDrawer.onRestoreDrawerState(savedInstanceState.getParcelable(EXTRA_STATE_MENUDRAWER));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(EXTRA_STATE_MENUDRAWER, mMenuDrawer.onSaveDrawerState());
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
			return;
		}
		super.onBackPressed();
	}

	@AfterInject
	protected void initMenuDrawer() {
		mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_CONTENT);
		mMenuDrawer.setMenuView(R.layout.menudrawer);
		mMenuDrawer.getMenuDrawer().setOffsetMenuEnabled(false);
	}

	@AfterViews
	protected void initMenuDrawerList() {
		mCategoriesListView = (ListView) findViewById(R.id.menudrawerCategoriesList);
		mCategoriesListView.setAdapter(mCategoriesListAdapter);

		mCategoriesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (id == Category.CATEGORY_ID_SCAN) {
					mCardScanner.initiateScan(MenuDrawerBaseActivity.this);
				} else if (id == Category.CATEGORY_ID_SEARCH) {
					onSearchRequested();
				} else {
					onMenuDrawerItemSelected(id);
					refreshMenuDrawer();
				}
				mMenuDrawer.closeMenu(true);
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
}
