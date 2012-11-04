package com.nilhcem.xebia.essentials.menudrawer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.res.ColorRes;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.model.Category;

@EBean
public class MenuDrawerListAdapter extends BaseAdapter {
	private List<Object> mItems;

	@RootContext
	protected Context mContext;

	@Bean
	protected InMemoryCache mCache;

	@StringRes(R.string.cards_list_all)
	protected String mAllCategoriesStr;

	@StringRes(R.string.drawer_categories)
	protected String mCategoriesTitleStr;

	@StringRes(R.string.drawer_scan)
	protected String mScanTitleStr;

	@StringRes(R.string.drawer_scan_card)
	protected String mScanCardStr;

	@ColorRes(R.color.xebia_purple)
	protected int mAllCategoriesColor;

	@ColorRes(R.color.drawer_item_selected)
	protected int mSelectedItemColor;

	@Override
	public int getCount() {
		if (mItems == null) {
			return 0;
		}
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		if (mItems == null) {
			return null;
		}
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		Object obj = getItem(position);
		if (obj instanceof Category) {
			return ((Category) obj).getId();
		}
		return 100 + position; // adding 100 to avoid collisions with real categories ids
	}

	@Override
	public int getItemViewType(int position) {
		Object obj = getItem(position);
		if (obj instanceof String) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItem(position) instanceof Category);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Object item = getItem(position);

		if (item instanceof String) {
			if (v == null) {
				v = MenuDrawerListCategoryView_.build(mContext);
			}

			MenuDrawerListCategoryView view = (MenuDrawerListCategoryView) v;
			view.bind((String) item);
		} else if (item instanceof Category) {
			if (v == null) {
				v = MenuDrawerListItemView_.build(mContext);
			}

			MenuDrawerListItemView view = (MenuDrawerListItemView) v;
			Category category = (Category) item;
			view.bind(category);

			// Highlight selected category
			long currentCategory = mCache.getSelectedCategory();
			if (category.getId() == currentCategory) {
				view.setBackgroundColor(mSelectedItemColor);
			} else {
				Compatibility.setDrawableToView(view, null);
			}
		}
		return v;
	}

	@AfterViews
	protected void initItems() {
		mItems = new ArrayList<Object>();

		// Categories
		mItems.add(mCategoriesTitleStr);
		Category all = new Category(Category.ALL_CATEGORIES_ID, mAllCategoriesStr, mAllCategoriesColor);
		mItems.add(all);
		for (Category category : mCache.getAllCategories()) {
			mItems.add(category);
		}

		// Scan card
		mItems.add(mScanTitleStr);
		Category scanButton = new Category(MenuDrawerListItemView.CATEGORY_SCAN_BUTTON, mScanCardStr, 0);
		mItems.add(scanButton);
	}
}
