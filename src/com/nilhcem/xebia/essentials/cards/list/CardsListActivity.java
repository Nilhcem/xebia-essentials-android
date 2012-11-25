package com.nilhcem.xebia.essentials.cards.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.IOnCardMenuSelected;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;
import com.nilhcem.xebia.essentials.core.dao.CardDao;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.menudrawer.MenuDrawerBaseActivity;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;

@EActivity(R.layout.main_layout)
public class CardsListActivity extends MenuDrawerBaseActivity implements IOnCardItemSelected, IOnCardMenuSelected {
	private static final Logger LOG = LoggerFactory.getLogger(CardsListActivity.class);
	private static final String LIST_FRAGMENT_TAG = "cardsListFragmentTag";
	public static final String EXTRA_SEARCH_QUERY = "CardsListActivity:searchQuery";

	@FragmentByTag(LIST_FRAGMENT_TAG)
	protected CardsListFragment mListFragment;

	@ViewById(R.id.cardsListViewPager)
	protected ViewPager mViewPager;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Card.class)
	protected CardDao mCardDao;

	@Extra(EXTRA_SEARCH_QUERY)
	protected String mSearchQuery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we rotate the device and are no more in two-pane layout mode, launch the CardsHtmlActivity
		if (!mIsMultipaned && mCache.isCardPositionSet()) {
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
		if (mCache.getCurrentCards() == null) {
			mCache.setCurrentCards(mCardDao.getAllCardsFromCategory(mCache.getSelectedCategory()));
		}
	}

	private void initViewPager() {
		if (mViewPager != null) {
			PagerAdapter adapter = new CardsPagerAdapter(this,
					getSupportFragmentManager(), mCache.getCurrentCards());
			mViewPager.setAdapter(adapter);
			mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				private volatile boolean setPosition = false;

				@Override
				public void onPageSelected(int position) {
					if (setPosition) {
						mCache.setCardPosition(position);
						refreshListView();
						setPosition = false;
					}
				}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				}

				@Override
				public void onPageScrollStateChanged(int state) {
					if (state == ViewPager.SCROLL_STATE_SETTLING) {
						setPosition = true;
					}
				}
			});

			if (mCache.isCardPositionSet()) {
				mViewPager.setCurrentItem(mCache.getCardPosition(), false);
			}
		}
	}

	private void initListFragment(boolean replaceFragment) {
		if (!replaceFragment && mListFragment != null) {
			mListFragment.setCards(mCache.getCurrentCards());
			return;
		}

		int resId = R.id.cardsListFragmentLayout;
		mListFragment = new CardsListFragment_();
		mListFragment.setCards(mCache.getCurrentCards());

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
		LOG.debug("Card selected: {}", position);
		mCache.setCardPosition(position);

		if (mIsMultipaned) {
			mViewPager.setCurrentItem(position, false);
			refreshListView();
		} else {
			Intent intent = new Intent(this, CardsHtmlActivity_.class);
			intent.putExtra(CardsHtmlActivity.EXTRA_CARD_POSITION, position);
			startActivity(intent);
		}
	}

	@Override
	protected void onMenuDrawerItemSelected(long id) {
		LOG.debug("Category #{} selected", id);
		mCache.setSelectedCategory(id);
		initAll(true);
	}

	@Override
	public void onBackPressed() {
		// If still some, refreshMenuDrawer(), otherwise onBackPressed
		if (isMenuDrawerClosedOrClosing() && mCache.getSelectedCategory() != Category.CATEGORY_ID_ALL) {
			onMenuDrawerItemSelected(Category.CATEGORY_ID_ALL);
			refreshMenuDrawer();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LOG.info("onSaveInstanceState");
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
		initViewPager();
	}

	private void refreshListView() {
		// Refresh selected item on listView
		if (mIsMultipaned) {
			try {
				mListFragment.getListView().invalidateViews();
			} catch (IllegalStateException e) {
				// Do nothing - content view not created yet
			}
		}
	}
}
