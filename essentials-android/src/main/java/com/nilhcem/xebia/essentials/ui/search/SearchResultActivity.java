package com.nilhcem.xebia.essentials.ui.search;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.ui.base.BaseActivity;
import com.nilhcem.xebia.essentials.ui.cards.CardsActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

public class SearchResultActivity extends BaseActivity {

    @Inject DataProvider mDataProvider;
    @Inject CardsDao mCardsDao;

    public SearchResultActivity() {
        super(0, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleSearchIntent(getIntent());
    }

    private void handleSearchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();

            if (data != null) {
                List<String> params = data.getPathSegments();
                if (params != null && params.size() == 1) {
                    String intentData = params.get(0);
                    Card card = mCardsDao.getCardByUrlId(intentData);
                    startDetailActivity(card);
                }
            }
        } else if (Intent.ACTION_SEARCH.equals(action)) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            List<Card> cards = mCardsDao.getCardsFromSearchQuery(searchQuery);

            int nbCards = cards.size();
            if (nbCards == 0) {
                Toast.makeText(this, R.string.intent_no_card_found, Toast.LENGTH_LONG).show();
                finish();
            } else if (nbCards == 1) {
                finish();
                startDetailActivity(cards.get(0));
            } else {
                startListingActivity(cards);
            }
        }
    }

    private void startListingActivity(List<Card> cards) {
        mDataProvider.setCardsFromSearchResult(cards);
        Intent intent = new Intent(this, CardsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startDetailActivity(Card card) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CARD, card);
        startActivity(intent);
    }
}
