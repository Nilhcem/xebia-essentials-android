package com.nilhcem.xebia.essentials.cards.list;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.res.DrawableRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.model.Card;

@EFragment
public class CardsListFragment extends SherlockListFragment {
	private List<Card> mCards;
	private IOnCardItemSelected mOnItemSelected;

	@Bean
	protected CardsListAdapter mAdapter;

	@DrawableRes(R.drawable.cards_list_divider)
	protected Drawable mDivider;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOnItemSelected = (IOnCardItemSelected) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOnItemSelected = null;
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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ListView listView = getListView();
		listView.setDivider(mDivider);
		listView.setDividerHeight(1);
		listView.requestFocus();
	}

	public void setCards(List<Card> cards) {
		mCards = cards;
	}
}
