package com.nilhcem.xebia.essentials.cards;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.bo.CardService;
import com.nilhcem.xebia.essentials.model.Card;

@EBean
public class CardsListAdapter extends BaseAdapter {
	private List<Card> cards;

	@RootContext
	protected Context context;

	@Bean
	protected CardService mCardService;

	@Override
	public int getCount() {
		return cards.size();
	}

	@Override
	public Card getItem(int position) {
		return cards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CardsListItemView cardItem;

		if (convertView == null) {
			cardItem = CardsListItemView_.build(context);
		} else {
			cardItem = (CardsListItemView) convertView;
		}

		Card card = getItem(position);
		cardItem.bind(card);
		cardItem.setTag(R.id.cardsListItemCategoryColor, card.getId());
		return cardItem;
	}

	/* package */ void setCategoryId(long id) {
		cards = mCardService.getDao().getAllCardsFromCategory(id);
	}
}
