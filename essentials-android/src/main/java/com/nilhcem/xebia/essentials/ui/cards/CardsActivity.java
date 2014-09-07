package com.nilhcem.xebia.essentials.ui.cards;

import android.content.Intent;
import android.os.Bundle;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.events.CardChangedEvent;
import com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.pager.DetailPagerFragment;
import com.nilhcem.xebia.essentials.ui.cards.listing.ListingFragment;
import com.nilhcem.xebia.essentials.ui.drawer.MenuDrawerActivity;

import hugo.weaving.DebugLog;

import static com.nilhcem.xebia.essentials.core.data.provider.DataProvider.CARD_POSITION_UNSET;
import static com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity.DETAIL_REQUEST_CODE;

public class CardsActivity extends MenuDrawerActivity {

    // Whether or not we are in dual-pane mode
    private boolean mIsDualPane;

    private ListingFragment mListingFragment;
    private DetailPagerFragment mDetailFragment;

    public CardsActivity() {
        super(R.layout.main_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);

        // find our fragments
        mListingFragment = (ListingFragment) getSupportFragmentManager().findFragmentById(R.id.main_listing_fragment);
        mDetailFragment = (DetailPagerFragment) getSupportFragmentManager().findFragmentById(R.id.main_detail_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // When we switch from a landscape 2-pane mode to a portrait single-pane mode
        int position = mDataProvider.getCurrentCardPosition(true);
        if (!mIsDualPane && position != CARD_POSITION_UNSET) {
            startDetailActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User pressed the back button from the DetailActivity page - reinitialize data
        if (requestCode == DETAIL_REQUEST_CODE && resultCode != RESULT_OK) {
            mDataProvider.setCurrentCardPosition(CARD_POSITION_UNSET);
        }
    }

    @DebugLog
    public void onEventMainThread(CardChangedEvent event) {
        if (!mIsDualPane) {
            startDetailActivity();
        }
    }

    private void startDetailActivity() {
        Intent i = new Intent(this, DetailActivity.class);
        startActivityForResult(i, DETAIL_REQUEST_CODE);
    }
}
