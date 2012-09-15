package com.nilhcem.xebia.essentials.cards.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.nilhcem.xebia.essentials.R;

@EFragment
public class CardsListFragment extends SherlockListFragment {
	private static final Logger LOG = LoggerFactory.getLogger(CardsListFragment.class);
	private static final String EXTRA_CATEGORY = "CardsListFragment:mCategoryId";

	private long mCategoryId = 0;

	@Bean
	protected CardsListAdapter mAdapter;

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

		Long cardId = (Long) v.getTag(R.id.cardsListItemCategoryColor);
		LOG.info("Card clicked: {}", cardId);

		IOnCardItemSelected activity = ((IOnCardItemSelected) getActivity());
		if (activity != null) {
			activity.onCardsListItemSelected(cardId);
		}
	}
}
