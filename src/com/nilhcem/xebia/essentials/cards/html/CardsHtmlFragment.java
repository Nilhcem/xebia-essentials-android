package com.nilhcem.xebia.essentials.cards.html;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
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
	protected InMemoryCache mCache;

	@StringRes(R.string.cards_url_prefix)
	protected String mUrlPrefix;

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
		setHasOptionsMenu(true);
		Category category = mCache.getCategoryById(mCard.getCategoryId());
		mTitle.setText(mCard.getTitle());
		mTitle.setBackgroundColor(category.getIntColor());
		mContent.setText(Html.fromHtml(mCard.getContent()));
		mContent.setMovementMethod(LinkMovementMethod.getInstance()); // make links clickable
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		menu.clear();
		inflater.inflate(R.menu.main_menu, menu);
		inflater.inflate(R.menu.cards_html_menu, menu);

		// Set the share intent
		ShareActionProvider shareProvider = (ShareActionProvider) menu
				.findItem(R.id.menu_share).getActionProvider();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, mCard.getTitle());
		shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s%s", mUrlPrefix, mCard.getUrl()));
		shareProvider.setShareIntent(shareIntent);
	}
}
