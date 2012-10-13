package com.nilhcem.xebia.essentials.cards.html;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nilhcem.xebia.essentials.cards.AbstractCardFragment;
import com.nilhcem.xebia.essentials.core.model.Card;

public class CardsPagerAdapter extends FragmentStatePagerAdapter {
	private List<Card> mCards;
	private WeakReference<Activity> mActivity;

	public CardsPagerAdapter(Activity activity, FragmentManager fm, List<Card> cards) {
		super(fm);
		mCards = cards;
		mActivity = new WeakReference<Activity>(activity);
	}

	@Override
	public Fragment getItem(int position) {
		Activity activity = null;
		if (mActivity != null) {
			activity = mActivity.get();
		}
		return AbstractCardFragment.newInstance(activity, mCards.get(position));
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
