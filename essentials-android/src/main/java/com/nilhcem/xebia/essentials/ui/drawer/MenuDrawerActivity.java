package com.nilhcem.xebia.essentials.ui.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.qrcode.ZxingIntegrator;
import com.nilhcem.xebia.essentials.events.CategoryChangedEvent;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;
import com.nilhcem.xebia.essentials.ui.about.AboutDialogFragment;
import com.nilhcem.xebia.essentials.ui.base.BaseActivity;
import com.nilhcem.xebia.essentials.ui.cards.detail.DetailActivity;

import javax.inject.Inject;

import butterknife.InjectView;
import hugo.weaving.DebugLog;

import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_ABOUT;
import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_ALL;
import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_SCAN;
import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_SEARCH;

public abstract class MenuDrawerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.content_frame) FrameLayout mMainLayoutContainer;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer) ListView mDrawerList;

    @Inject ZxingIntegrator mQrCode;

    private final int mSubLayoutResId;
    private ActionBarDrawerToggle mDrawerToggle;
    private MenuDrawerAdapter mDrawerAdapter;

    public MenuDrawerActivity(int layoutResId) {
        super(R.layout.menudrawer_layout, true);
        mSubLayoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);

        mMainLayoutContainer.removeAllViews();
        mMainLayoutContainer.addView(getLayoutInflater().inflate(mSubLayoutResId, null, true));

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerAdapter = new MenuDrawerAdapter(this);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int categoryId = ((Category) mDrawerAdapter.getItem(position)).getIntId();
        if (CATEGORY_ID_SCAN == categoryId) {
            mQrCode.initiateScan(this);
        } else if (CATEGORY_ID_SEARCH == categoryId) {
            onSearchRequested();
        } else if (CATEGORY_ID_ABOUT == categoryId) {
            new AboutDialogFragment().show(getSupportFragmentManager(), AboutDialogFragment.TAG);
        } else {
            mDataProvider.setCurrentCategoryId(categoryId);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        int currentCategoryId = mDataProvider.getCurrentCategoryId();
        if (CATEGORY_ID_ALL == currentCategoryId) {
            super.onBackPressed();
        } else {
            mDataProvider.setCurrentCategoryId(CATEGORY_ID_ALL);
        }
    }

    @DebugLog
    public void onEventMainThread(CategoryChangedEvent event) {
        mDrawerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZxingIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            Card card = mQrCode.onActivityResult(requestCode, resultCode, data);
            if (card == null) {
                Toast.makeText(this, R.string.intent_no_card_found, Toast.LENGTH_SHORT).show();
            } else {
                startDetailActivity(card);
            }
        }
    }

    private void startDetailActivity(Card card) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CARD, card);
        startActivity(intent);
    }
}
