package com.nilhcem.xebia.essentials.cards.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EFragment
public class CardsListFragment extends SherlockListFragment {
	public static final String EXTRA_CATEGORY = "CardsListFragment:mCategoryId";

	private List<Card> mCards = Collections.synchronizedList(new ArrayList<Card>());
	private long mCategoryId = Category.ALL_CATEGORIES_ID;
	private IOnCardItemSelected mOnItemSelected;

	@Bean
	protected CardsListAdapter mAdapter;

	@Bean
	protected CardService mCardService;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOnItemSelected = (IOnCardItemSelected) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

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
		mCards.clear();
		List<Card> cards = mCardService.getDao().getAllCardsFromCategory(mCategoryId);
		mCards.addAll(cards);
		mAdapter.init(mCards);
		mAdapter.notifyDataSetChanged();

		if (mOnItemSelected != null) {
			mOnItemSelected.onCardsFinishedLoading();
		}
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

	public List<Card> getCards() {
		return mCards;
	}
}
