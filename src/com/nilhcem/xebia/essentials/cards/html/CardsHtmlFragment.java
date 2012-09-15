package com.nilhcem.xebia.essentials.cards.html;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.InMemoryCategoryFinder;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EFragment(R.layout.cards_html_fragment)
public class CardsHtmlFragment extends SherlockFragment {
	private static final String EXTRA_CARD = "CardsHtmlFragment:mCard";

	@ViewById(R.id.cardsHtmlScroll)
	protected ScrollView mScroll;

	@ViewById(R.id.cardsHtmlTitle)
	protected TextView mTitle;

	@ViewById(R.id.cardsHtmlContent)
	protected TextView mContent;

	@Bean
	protected CardService mCardService;

	@Bean
	protected InMemoryCategoryFinder mCategoryFinder;

	private Card mCard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(EXTRA_CARD)) {
			mCard = savedInstanceState.getParcelable(EXTRA_CARD);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(EXTRA_CARD, mCard);
	}

	public static CardsHtmlFragment newInstance(Card card) {
		CardsHtmlFragment fragment = new CardsHtmlFragment_();
		fragment.mCard = card;
		return fragment;
	}

	@AfterViews
	protected void initCardData() {
		Category category = mCategoryFinder.getById(mCard.getCategoryId());
		mTitle.setText(mCard.getTitle());
		mTitle.setBackgroundColor(category.getIntColor());
		mContent.setText(Html.fromHtml(mCard.getContent()));
		mContent.setMovementMethod(LinkMovementMethod.getInstance()); // make links clickable
	}
}
