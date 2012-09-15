package com.nilhcem.xebia.essentials.cards.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

@EFragment
public class CardsListFragment extends SherlockListFragment {
	private static final String EXTRA_CATEGORY = "CardsListFragment:mCategoryId";

	private long mCategoryId = 0;
	private IOnCardItemSelected mOnItemSelected;

	@Bean
	protected CardsListAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOnItemSelected = (IOnCardItemSelected) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(EXTRA_CATEGORY)) {
			mCategoryId = savedInstanceState.getLong(EXTRA_CATEGORY);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(EXTRA_CATEGORY, mCategoryId);
	}

	@AfterViews
	protected void bindAdapter() {
		setListAdapter(mAdapter);
		init(mCategoryId);
	}

	@UiThread
	public void init(long categoryId) {
		mCategoryId = categoryId;
		mAdapter.init(categoryId);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (mOnItemSelected != null) {
			mOnItemSelected.onCardsListItemSelected(position);
		}
	}

	public long getCategoryId() {
		return mCategoryId;
	}
}
