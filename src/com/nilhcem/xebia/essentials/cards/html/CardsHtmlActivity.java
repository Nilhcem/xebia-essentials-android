package com.nilhcem.xebia.essentials.cards.html;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.IOnCardMenuSelected;
import com.nilhcem.xebia.essentials.cards.list.*;
import com.nilhcem.xebia.essentials.core.BaseActivity;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;
import com.nilhcem.xebia.essentials.core.dao.CardDao;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;

@EActivity(R.layout.cards_html)
public class CardsHtmlActivity extends BaseActivity implements IOnCardMenuSelected {
	public static final String EXTRA_ONE_CARD_ONLY = "CardsHtmlActivity:oneCardOnly";
	public static final String EXTRA_CARD_POSITION = "CardsHtmlActivity:cardPosition";

	public static final int DISPLAY_FROM_CATEGORY = 1;
	public static final int DISPLAY_ONE_CARD = 2;

	@ViewById(R.id.cardsHtmlViewPager)
	protected ViewPager mViewPager;
	private CardsPagerAdapter mViewPagerAdapter;

	@Extra(CardsHtmlActivity.EXTRA_ONE_CARD_ONLY)
	protected Long mCard;

	@InstanceState
	@Extra(CardsHtmlActivity.EXTRA_CARD_POSITION)
	protected int mCardPosition;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Card.class)
	protected CardDao mCardDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we rotate the device and now are in two-pane layout mode, this
		// activity is no longer necessary
		if (mCard == null && mIsMultipaned) {
			mCache.setCardPosition(mCardPosition);
			finish();
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mViewPager.requestFocus();
	}

	@AfterViews
	protected void initActivity() {
		List<Card> cards = getCardsFromIntent();
		mViewPagerAdapter = new CardsPagerAdapter(this,
				getSupportFragmentManager(), cards);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(mCardPosition, false);
	}

	private List<Card> getCardsFromIntent() {
		List<Card> cards = null;

		if (mCard == null) {
			cards = mCache.getCurrentCards();
		} else {
			Card card = mCardDao.getById(mCard);
			cards = new ArrayList<Card>();
			cards.add(card);
		}
		return cards;
	}

	@AfterInject
	protected void initHomeButton() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mCache.setSelectedCategory(Category.CATEGORY_ID_ALL);
			Intent intent = new Intent(this, CardsListActivity_.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mCardPosition = mViewPager.getCurrentItem();
	}

	@UiThread
	@Override
	public void onCardMenuSelected() {
		SettingsActivity.switchViewMode(this);

		// Force viewpager to refresh itself
		int position = mViewPager.getCurrentItem();
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(position, false);
	}
}
