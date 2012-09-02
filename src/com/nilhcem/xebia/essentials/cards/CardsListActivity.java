package com.nilhcem.xebia.essentials.cards;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.InMemoryCategoryFinder;
import com.nilhcem.xebia.essentials.model.Category;
import com.viewpagerindicator.TabPageIndicator;

@EActivity(R.layout.cards_list)
public class CardsListActivity extends FragmentActivity {
	@ViewById(R.id.mainViewPager)
	protected ViewPager mViewPager;

	@ViewById(R.id.mainViewIndicator)
	protected TabPageIndicator mViewPagerIndicator;

	private CategoriesPagerAdapter mViewPagerAdapter;

	@Bean
	protected InMemoryCategoryFinder mMemoryFinder;

	@StringRes(R.string.cards_list_all)
	protected String mAll;

	@AfterViews
	protected void createViewPagerAdapter() {
		mViewPagerAdapter = new CategoriesPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPagerIndicator.setViewPager(mViewPager);
	}

	private class CategoriesPagerAdapter extends FragmentPagerAdapter {
		private List<Category> mCategories;

		public CategoriesPagerAdapter(FragmentManager fm) {
			super(fm);
			mCategories = mMemoryFinder.getAll();
		}

		@Override
		public Fragment getItem(int position) {
			long catId;
			if (position == 0) {
				catId = 0;
			} else {
				catId = mCategories.get(position - 1).getId();
			}
			return CardsListFragment.newInstance(catId);
		}

		@Override
		public int getCount() {
			return mCategories.size() + 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return mAll;
			} else {
				return mCategories.get(position - 1).getName();
			}
		}
	}
}
