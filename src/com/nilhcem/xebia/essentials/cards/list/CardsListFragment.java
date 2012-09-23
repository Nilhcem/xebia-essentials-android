package com.nilhcem.xebia.essentials.cards.list;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.nilhcem.xebia.essentials.core.model.Card;

@EFragment
public class CardsListFragment extends SherlockListFragment {
	private List<Card> mCards;
	private IOnCardItemSelected mOnItemSelected;

	@Bean
	protected CardsListAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOnItemSelected = (IOnCardItemSelected) activity;
	}

	@AfterViews
	protected void bindAdapter() {
		mAdapter.init(mCards);
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (mOnItemSelected != null) {
			mOnItemSelected.onCardsListItemSelected(position);
		}
	}

	public void setCards(List<Card> cards) {
		mCards = cards;
	}
}
