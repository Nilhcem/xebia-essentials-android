package com.nilhcem.xebia.essentials.cards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.nilhcem.xebia.essentials.R;

@EFragment
public class CardsListFragment extends ListFragment {
	private static final Logger LOG = LoggerFactory.getLogger(CardsListFragment.class);
	private static final String KEY_CATEGORY = "CardsListFragment:mCategoryId";

	private long mCategoryId;

	@Bean
	protected CardsListAdapter mAdapter;

	public static CardsListFragment newInstance(long category) {
		CardsListFragment fragment = new CardsListFragment_();
		fragment.mCategoryId = category;
		return fragment;
	}

	@AfterViews
	protected void bindAdapter() {
		mAdapter.setCategoryId(mCategoryId);
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		LOG.info("Card clicked: {}", (Long) v.getTag(R.id.cardsListItemCategoryColor));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CATEGORY)) {
			mCategoryId = savedInstanceState.getLong(KEY_CATEGORY);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(KEY_CATEGORY, mCategoryId);
	}
}
