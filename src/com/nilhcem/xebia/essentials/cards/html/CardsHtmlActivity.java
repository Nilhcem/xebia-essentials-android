package com.nilhcem.xebia.essentials.cards.html;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.BaseActivity;

@EActivity(R.layout.cards_html_activity)
public class CardsHtmlActivity extends BaseActivity {
	public static final String INTENT_CARD_ID = "cardId";

	@Extra(CardsHtmlActivity.INTENT_CARD_ID)
	protected Long mCardId;

	@FragmentById(R.id.cardsHtmlFragment)
	protected CardsHtmlFragment mHtmlFragment;

	@AfterViews
	protected void sendCardIdToFragment() {
		mHtmlFragment.initCard(mCardId);
	}
}
