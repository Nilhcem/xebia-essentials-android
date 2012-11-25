package com.nilhcem.xebia.essentials.cards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.flip.*;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;

@EFragment
@OptionsMenu(R.menu.cards_html_menu)
public abstract class AbstractCardFragment extends SherlockFragment {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractCardFragment.class);

	@Bean
	protected InMemoryCache mCache;

	@InstanceState
	protected Card mCard;

	private String mUrlPrefix;
	private IOnCardMenuSelected mOnMenuSelected;

	public static AbstractCardFragment newInstance(Activity activity, Card card) {
		AbstractCardFragment fragment;
		int viewMode = SettingsActivity.getViewMode(activity);

		if (viewMode == SettingsActivity.VIEW_MODE_CARD) {
			fragment = new CardsFlipFragment_();
		} else { // SettingsActivity.VIEW_MODE_DETAILS
			fragment = new CardsHtmlFragment_();
		}
		fragment.mCard = card;
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		LOG.debug("onAttach()");
		super.onAttach(activity);
		mOnMenuSelected = (IOnCardMenuSelected) activity;
		mUrlPrefix = String.format("http://%s", activity.getString(R.string.cards_url_prefix));
	}

	@Override
	public void onDetach() {
		LOG.debug("onDetach()");
		super.onDetach();
		mOnMenuSelected = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		ShareActionProvider shareProvider = (ShareActionProvider) menu
				.findItem(R.id.menu_share).getActionProvider();
		if (mCard != null) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, mCard.getTitle());
			shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s%s", mUrlPrefix, mCard.getUrl()));
			shareProvider.setShareIntent(shareIntent);
		}
	}

	@OptionsItem({ R.id.menu_see_card, R.id.menu_see_details })
	protected void onMenuCardSelected() {
		mOnMenuSelected.onCardMenuSelected();
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

	@AfterViews
	protected void setHasOptionMenu() {
		setHasOptionsMenu(true);
	}
}
