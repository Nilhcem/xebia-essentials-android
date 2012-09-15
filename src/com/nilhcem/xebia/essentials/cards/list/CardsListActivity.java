package com.nilhcem.xebia.essentials.cards.list;

import android.content.Intent;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.dashboard.*;

@EActivity(R.layout.cards_list)
public class CardsListActivity extends DashboardBaseActivity_ implements IOnCardItemSelected {
	@FragmentById(R.id.cardsListFragment)
	protected CardsListFragment mListFragment;

	@Override
	public void onCardsListItemSelected(Long cardId) {
		Intent intent = new Intent(this, CardsHtmlActivity_.class);
		intent.putExtra(CardsHtmlActivity.INTENT_CARD_ID, cardId);
		startActivity(intent);
	}

	@Override
	protected void onDashboardItemSelected(long id) {
		mListFragment.init(id);
	}
}
