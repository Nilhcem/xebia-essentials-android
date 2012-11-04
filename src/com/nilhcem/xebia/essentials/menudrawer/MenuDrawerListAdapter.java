package com.nilhcem.xebia.essentials.menudrawer;

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
	private List<Category> mCategories;

	@RootContext
	protected Context mContext;

	@Bean
	protected InMemoryCache mCache;

	@StringRes(R.string.cards_list_all)
	protected String mAllCategoriesStr;

	@ColorRes(R.color.xebia_purple)
	protected int mAllCategoriesColor;

	@ColorRes(R.color.drawer_item_selected)
	protected int mSelectedItemColor;

	@Override
	public int getCount() {
		if (mCategories == null) {
			return 0;
		}
		return mCategories.size();
	}

	@Override
	public Category getItem(int position) {
		if (mCategories == null) {
			return null;
		}
		return mCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuDrawerListItemView view;

		if (convertView == null) {
			view = MenuDrawerListItemView_.build(mContext);
		} else {
			view = (MenuDrawerListItemView) convertView;
		}

		Category category = getItem(position);
		view.bind(category);

		// Highlight selected category
		long currentCategory = mCache.getSelectedCategory();
		if (category.getId() == currentCategory) {
			view.setBackgroundColor(mSelectedItemColor);
		} else {
			Compatibility.setDrawableToView(view, null);
		}
		return view;
	}

	@AfterViews
	protected void initCategories() {
		Category all = new Category(Category.ALL_CATEGORIES_ID, mAllCategoriesStr, mAllCategoriesColor);
		mCategories = mCache.getAllCategories();
		if (mCategories != null) {
			mCategories.add(0, all);
		}
	}
}
