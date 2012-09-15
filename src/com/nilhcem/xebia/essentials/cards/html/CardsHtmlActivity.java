package com.nilhcem.xebia.essentials.cards.html;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.BaseActivity;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;

@EActivity(R.layout.cards_html)
public class CardsHtmlActivity extends BaseActivity {
	public static final String INTENT_ITEM_ID = "itemId"; // Card or Category ID, depending on the INTENT_DISPLAY_TYPE
	public static final String INTENT_DISPLAY_TYPE = "displayType"; // See DISPLAY_*
	public static final String INTENT_CARD_POSITION = "cardPosition";

	public static final int DISPLAY_FROM_CATEGORY = 1;
	public static final int DISPLAY_ONE_CARD = 2;

	@ViewById(R.id.cardsHtmlViewPager)
	protected ViewPager mViewPager;
	private CardsPagerAdapter mViewPagerAdapter;

	@Extra(CardsHtmlActivity.INTENT_ITEM_ID)
	protected Long mItemId;

	@Extra(CardsHtmlActivity.INTENT_DISPLAY_TYPE)
	protected int mDisplayType;

	@Extra(CardsHtmlActivity.INTENT_CARD_POSITION)
	protected int mCardPosition;

	@Bean
	protected CardService mCardService;

	@AfterViews
	@Background
	protected void initActivity() {
		List<Card> cards = getCardsFromIntent();
		createViewPagerAdapter(cards);
	}

	@UiThread
	protected void createViewPagerAdapter(List<Card> cards) {
		mViewPagerAdapter = new CardsPagerAdapter(getSupportFragmentManager(), cards);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(mCardPosition, false);
	}

	private List<Card> getCardsFromIntent() {
		List<Card> cards = null;

		switch (mDisplayType) {
			case DISPLAY_FROM_CATEGORY:
				cards = mCardService.getDao().getAllCardsFromCategory(mItemId);
				break;
			default: //DISPLAY_ONE_CARD
				Card card = mCardService.getDao().getById(mItemId);
				cards = new ArrayList<Card>();
				cards.add(card);
				break;
		}

		return cards;
	}
}
