package com.nilhcem.xebia.essentials.ui.cards.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.ui.base.BaseActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.pager.DetailPagerFragment;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

public class DetailActivity extends BaseActivity {

    public static final int DETAIL_REQUEST_CODE = 1;
    public static final String EXTRA_CARD = "mCard";

    @Inject CardsDao mCardsDao;

    public DetailActivity() {
        super(0, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(boolean forceRefreshingFragment) {
        Card card = getCardFromOptionalIntent(getIntent());

        // If we are in two-pane layout mode, this activity is no longer necessary
        if (card == null && getResources().getBoolean(R.bool.has_two_panes)) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        // Place a DetailFragment as our content pane
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DetailPagerFragment fragment = (DetailPagerFragment) fm.findFragmentByTag(DetailPagerFragment.TAG);
        if (forceRefreshingFragment && fragment != null) {
            ft.remove(fragment);
            fragment = null;
        }
        if (fragment == null) {
            fragment = DetailPagerFragment.newInstance(card);
        }

        ft.replace(android.R.id.content, fragment, DetailPagerFragment.TAG).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @DebugLog
    private Card getCardFromOptionalIntent(Intent intent) {
        if (intent == null) {
            return null;
        }

        Card card = null;
        if (intent.hasExtra(EXTRA_CARD)) {
            card = intent.getParcelableExtra(EXTRA_CARD);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                List<String> params = data.getPathSegments();
                if (params != null && params.size() == 1) {
                    String intentData = params.get(0);
                    card = mCardsDao.getCardByUrlId(intentData.toLowerCase(Locale.US));
                }
            }
        }
        return card;
    }

    private void navigateUp() {
        mDataProvider.resetData();
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }
}
