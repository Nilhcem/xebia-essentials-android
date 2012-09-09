package com.nilhcem.xebia.essentials.cards.html;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.InMemoryCategoryFinder;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EFragment(R.layout.cards_html_fragment)
public class CardsHtmlFragment extends Fragment {
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

	@Background
	public void initCard(Long cardId) {
		Card card = mCardService.getDao().getById(cardId);
		initCardData(card);
	}

	@UiThread
	protected void initCardData(Card card) {
		Category category = mCategoryFinder.getById(card.getCategoryId());
		mTitle.setText(card.getTitle());
		mTitle.setBackgroundColor(category.getIntColor());
		mContent.setText(Html.fromHtml(card.getContent()));
		mContent.setMovementMethod(LinkMovementMethod.getInstance()); // make links clickable
		mScroll.smoothScrollTo(0, 0);
	}
}
