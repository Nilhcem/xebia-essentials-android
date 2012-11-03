package com.nilhcem.xebia.essentials.cards.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.IOnCardMenuSelected;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.dashboard.*;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;

@EActivity(R.layout.main_layout)
public class CardsListActivity extends DashboardBaseActivity_ implements IOnCardItemSelected, IOnCardMenuSelected {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardsListActivity.class);
	private static final String LIST_FRAGMENT_TAG = "cardsListFragmentTag";

	private List<Card> mCards = Collections.synchronizedList(new ArrayList<Card>());

	@InstanceState
	protected long mCategoryId = Category.ALL_CATEGORIES_ID;

	@FragmentByTag(LIST_FRAGMENT_TAG)
	protected CardsListFragment mListFragment;

	@ViewById(R.id.cardsListViewPager)
	protected ViewPager mViewPager;

	@Bean
	protected CardService mCardService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we rotate the device and are no more in two-pane layout mode, launch the CardsHtmlActivity
		if (!mIsMultipaned && mCache.getCardPosition() > 0) {
			onCardsListItemSelected(mCache.getCardPosition());
		}
	}

	@AfterViews
	protected void initAll() {
		initAll(false);
	}

	private void initAll(boolean replaceFragment) {
		initCards();
		initViewPager();
		initListFragment(replaceFragment);
	}

	private void initCards() {
		mCards.clear();
		mCards.addAll(mCardService.getDao().getAllCardsFromCategory(mCategoryId));
		LOGGER.debug("Cards size: {}", mCards.size());
	}

	private void initViewPager() {
		if (mViewPager != null) {
			PagerAdapter adapter = new CardsPagerAdapter(this,
					getSupportFragmentManager(), mCards);
			mViewPager.setAdapter(adapter);

			if (mCache.getCardPosition() != 0) {
				mViewPager.setCurrentItem(mCache.getCardPosition(), false);
			}
		}
	}

	private void initListFragment(boolean replaceFragment) {
		if (!replaceFragment && mListFragment != null) {
			mListFragment.setCards(mCards);
			return;
		}

		int resId = R.id.cardsListFragmentLayout;
		mListFragment = new CardsListFragment_();
		mListFragment.setCards(mCards);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if (replaceFragment) {
			transaction.replace(resId, mListFragment, LIST_FRAGMENT_TAG);
		} else {
			transaction.add(resId, mListFragment, LIST_FRAGMENT_TAG);
		}
		transaction.commit();
	}

	@Override
	public void onCardsListItemSelected(int position) {
		LOGGER.debug("Card selected: {}", position);

		if (mIsMultipaned) {
			mViewPager.setCurrentItem(position);
		} else {
			mCache.setCardPosition(position);
			Intent intent = new Intent(this, CardsHtmlActivity_.class);
			intent.putExtra(CardsHtmlActivity.INTENT_CARD_POSITION, position);
			intent.putExtra(CardsHtmlActivity.INTENT_DISPLAY_TYPE,
					CardsHtmlActivity.DISPLAY_FROM_CATEGORY);
			intent.putExtra(CardsHtmlActivity.INTENT_ITEM_ID, mCategoryId);
			startActivity(intent);
		}
	}

	@Override
	protected void onDashboardItemSelected(long id) {
		LOGGER.debug("Category #{} selected", id);
		mCategoryId = id;
		mCache.resetCardPosition();
		initAll(true);
	}

	@Override
	public void onBackPressed() {
		if (!mDrawerGarment.isDrawerOpened()
				&& mCategoryId != Category.ALL_CATEGORIES_ID) {
			mCache.resetCardPosition();
			onDashboardItemSelected(Category.ALL_CATEGORIES_ID);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LOGGER.info("onSaveInstanceState");
		if (mViewPager != null) {
			mCache.setCardPosition(mViewPager.getCurrentItem());
			mViewPager.setAdapter(null); // Remove adapter reference so that no fragment state will be saved - Avoid memory leaks on 7inch layout
		}
		if (!mIsMultipaned) {
			mCache.resetCardPosition();
		}
		// When Activity is destroyed and retains state (like when you turn the device to landscape mode),
		// it doesn't retain the exact Fragment objects in the stack, only their states - Fragment.FragmentState objects,
		// i.e. actual fragments in the back stack are re-created every time activity gets re-created with retained state.
		// http://stackoverflow.com/questions/8482606/when-a-fragment-is-replaced-and-put-in-the-back-stack-or-removed-does-it-stay
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		initViewPager(); // Since we removed adapter reference, recreate it
	}

	@UiThread
	@Override
	public void onCardMenuSelected() {
		SettingsActivity.switchViewMode(this);
		mCache.setCardPosition(mViewPager.getCurrentItem());
		initViewPager();
	}
}
