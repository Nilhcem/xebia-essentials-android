package com.nilhcem.xebia.essentials.cards.html;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nilhcem.xebia.essentials.cards.AbstractCardFragment;
import com.nilhcem.xebia.essentials.core.model.Card;

public class CardsPagerAdapter extends FragmentStatePagerAdapter {
	private List<Card> mCards;

	public CardsPagerAdapter(FragmentManager fm, List<Card> cards) {
		super(fm);
		mCards = cards;
	}

	@Override
	public Fragment getItem(int position) {
		return AbstractCardFragment.newInstance(mCards.get(position));
	}

	@Override
	public int getCount() {
		return mCards.size();
	}

	/**
	 * Forces item to be destroyed and recreated, so that ViewPager can be updated dynamically.
	 *
	 * @see http://stackoverflow.com/questions/10849552/android-viewpager-cant-update-dynamically
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
