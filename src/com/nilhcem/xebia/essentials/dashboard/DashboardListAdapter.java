package com.nilhcem.xebia.essentials.dashboard;

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
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.model.Category;

@EBean
public class DashboardListAdapter extends BaseAdapter {
	private List<Category> mCategories;

	@RootContext
	protected Context mContext;

	@Bean
	protected InMemoryCache mCache;

	@StringRes(R.string.cards_list_all)
	protected String mAllCategoriesStr;

	@ColorRes(R.color.xebia_purple)
	protected int mAllCategoriesColor;

	@Override
	public int getCount() {
		return mCategories.size();
	}

	@Override
	public Category getItem(int position) {
		return mCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DashboardListItemView dashboardItem;

		if (convertView == null) {
			dashboardItem = DashboardListItemView_.build(mContext);
		} else {
			dashboardItem = (DashboardListItemView) convertView;
		}

		Category category = getItem(position);
		dashboardItem.bind(category);
		return dashboardItem;
	}

	@AfterViews
	protected void initCategories() {
		Category all = new Category(Category.ALL_CATEGORIES_ID, mAllCategoriesStr, mAllCategoriesColor);
		mCategories = mCache.getAllCategories();
		mCategories.add(0, all);
	}
}
