package com.nilhcem.xebia.essentials.cards.list;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.dashboard.*;

@EActivity(R.layout.main_layout)
public class CardsListActivity extends DashboardBaseActivity_ implements IOnCardItemSelected {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardsListActivity.class);

	@FragmentById(R.id.cardsListFragment)
	protected CardsListFragment mListFragment;

	// Only for multipaned
	@ViewById(R.id.cardsListViewPager)
	protected ViewPager mViewPager;

	@BooleanRes(R.bool.multipaned)
	protected boolean mIsMultipaned;

	protected boolean mRefreshActivity;

	@Override
	public void onCardsListItemSelected(int position) {
		LOGGER.info("Card selected: {}", position);

		if (mIsMultipaned) {
			mViewPager.setCurrentItem(position);
		} else {
			Intent intent = new Intent(this, CardsHtmlActivity_.class);
			intent.putExtra(CardsHtmlActivity.INTENT_CARD_POSITION, position);
			intent.putExtra(CardsHtmlActivity.INTENT_DISPLAY_TYPE,
					CardsHtmlActivity.DISPLAY_FROM_CATEGORY);
			intent.putExtra(CardsHtmlActivity.INTENT_ITEM_ID,
					mListFragment.getCategoryId());
			startActivity(intent);
		}
	}

	@Override
	public void onCardsFinishedLoading() {
		initViewPager();
	}

	@Override
	protected void onDashboardItemSelected(long id) {
		LOGGER.debug("Item #{} selected", id);
		mListFragment.init(id);
	}

	@AfterViews
	protected void initViewPager() {
		if (mViewPager != null) {
			List<Card> cards = mListFragment.getCards();
			PagerAdapter adapter = new CardsPagerAdapter(
					getSupportFragmentManager(), cards);
			mViewPager.setAdapter(adapter);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	}

}
