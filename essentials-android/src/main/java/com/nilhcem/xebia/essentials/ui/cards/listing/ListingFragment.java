package com.nilhcem.xebia.essentials.ui.cards.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.events.CardChangedEvent;
import com.nilhcem.xebia.essentials.events.CategoryChangedEvent;
import com.nilhcem.xebia.essentials.ui.base.BaseFragment;
import com.nilhcem.xebia.essentials.ui.cards.random.RandomMenuHelper;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

public class ListingFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListingAdapter mAdapter;
    private ListView mListView;
    private boolean mIsDualPane;

    @Inject RandomMenuHelper mRandomMenuHelper;

    public ListingFragment() {
        super(0, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDualPane = getResources().getBoolean(R.bool.has_two_panes);
        setHasOptionsMenu(!mIsDualPane);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new ListingAdapter(getActivity(), mIsDualPane);
        mListView = new ListView(getActivity());

        // Dividers are set in the views directly. So we don't have an unnecessary gap between categories colors
        mListView.setDivider(null);
        mListView.setDividerHeight(0);

        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        return mListView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mRandomMenuHelper.onCreateOptionsMenu(getActivity(), menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mRandomMenuHelper.onOptionsItemSelected(getActivity(), item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mDataProvider.setCurrentCardPosition(position);
    }

    @DebugLog
    public void onEventMainThread(CardChangedEvent event) {
        if (mIsDualPane) {
            mAdapter.notifyDataSetChanged();
            int pos = event.cardPosition;
            if (mListView.getFirstVisiblePosition() >= pos || mListView.getLastVisiblePosition() <= pos) {
                mListView.smoothScrollToPosition(pos);
            }
        }
    }

    @DebugLog
    public void onEventMainThread(CategoryChangedEvent event) {
        mAdapter.notifyDataSetChanged();
        if (!mIsDualPane) {
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(0);
                }
            }, 100L);
        }
    }

    public ListView getListView() {
        return mListView;
    }
}
