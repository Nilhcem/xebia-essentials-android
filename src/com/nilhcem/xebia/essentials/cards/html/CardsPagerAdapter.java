package com.nilhcem.xebia.essentials.cards.html;

import java.util.List;

import com.nilhcem.xebia.essentials.core.model.Card;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CardsPagerAdapter extends FragmentPagerAdapter {
	private List<Card> mCards;

	public CardsPagerAdapter(FragmentManager fm, List<Card> cards) {
		super(fm);
		mCards = cards;
	}

	@Override
	public Fragment getItem(int position) {
		return CardsHtmlFragment.newInstance(mCards.get(position));
	}

	@Override
	public int getCount() {
		return mCards.size();
	}
}
