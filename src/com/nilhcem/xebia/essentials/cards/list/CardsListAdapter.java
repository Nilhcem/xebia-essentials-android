package com.nilhcem.xebia.essentials.cards.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EBean
public class CardsListAdapter extends BaseAdapter {
	private List<Card> mCards;

	@RootContext
	protected Context mContext;

	@Bean
	protected InMemoryCache mCache;

	@BooleanRes(R.bool.multipaned)
	protected boolean mIsMultipaned;

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

		// Set background color
		Category category = null;
		int cachedPosition = -1;
		if (mIsMultipaned) {
			category = mCache.getCategoryById(card.getCategoryId());
			cachedPosition = mCache.getCardPosition();
			if (cachedPosition < 0) {
				cachedPosition = 0;
			}
		}
		if (category != null && position == cachedPosition) {
			cardItem.setBackgroundColor(category.getIntColor() - 0xee000000);
		} else {
			Compatibility.setDrawableToView(cardItem, null);
		}

		cardItem.bind(card);
		return cardItem;
	}

	public void init(List<Card> cards) {
		mCards = cards;
	}
}
