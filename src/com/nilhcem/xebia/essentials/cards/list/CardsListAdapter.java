package com.nilhcem.xebia.essentials.cards.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.nilhcem.xebia.essentials.core.model.Card;

@EBean
public class CardsListAdapter extends BaseAdapter {
	private List<Card> mCards;

	@RootContext
	protected Context mContext;

	@Override
	public int getCount() {
		if (mCards == null) {
			return 0;
		}
		return mCards.size();
	}

	@Override
	public Card getItem(int position) {
		if (mCards == null) {
			return null;
		}
		return mCards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CardsListItemView cardItem;

		if (convertView == null) {
			cardItem = CardsListItemView_.build(mContext);
		} else {
			cardItem = (CardsListItemView) convertView;
		}

		Card card = getItem(position);
		cardItem.bind(card);
		return cardItem;
	}

	public void init(List<Card> cards) {
		mCards = cards;
	}
}
