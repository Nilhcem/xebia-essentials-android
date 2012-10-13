package com.nilhcem.xebia.essentials.cards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.flip.*;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;

public abstract class AbstractCardFragment extends SherlockFragment {
	private static final String EXTRA_CARD = "AbstractCardFragment:mCard";

	protected Card mCard;

	private String mUrlPrefix;
	private IOnCardMenuSelected mOnMenuSelected;

	public static AbstractCardFragment newInstance(Card card) {
		AbstractCardFragment fragment;
		int viewMode = SettingsActivity.getViewMode();

		if (viewMode == SettingsActivity.VIEW_MODE_CARD) {
			fragment = new CardsFlipFragment_();
		} else { // SettingsActivity.VIEW_MODE_DETAILS
			fragment = new CardsHtmlFragment_();
		}
		fragment.mCard = card;
		return fragment;
	}

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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mOnMenuSelected = (IOnCardMenuSelected) activity;
		mUrlPrefix = String.format("http://%s", activity.getString(R.string.cards_url_prefix));
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.menu_see_card || id == R.id.menu_see_details) {
			mOnMenuSelected.onCardMenuSelected();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem details = menu.findItem(R.id.menu_see_details);
		MenuItem card = menu.findItem(R.id.menu_see_card);

		boolean isDetailsView = (this instanceof CardsHtmlFragment_);
		card.setVisible(isDetailsView);
		details.setVisible(!isDetailsView);
	}
}
