package com.nilhcem.xebia.essentials.ui.cards.detail.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.ui.cards.detail.BaseDetailFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    @Inject DataProvider mDataProvider;

    private final List<Card> mCards = new ArrayList<>();

    public DetailPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        EssentialsApplication.get(context).inject(this);
    }

    @Override
    public Fragment getItem(int position) {
        return BaseDetailFragment.newInstance(mCards.get(position), mDataProvider.shouldDisplayCardView());
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // http://stackoverflow.com/questions/10849552/android-viewpager-cant-update-dynamically
        return POSITION_NONE;
    }

    public void updateItems(List<Card> cards) {
        mCards.clear();
        mCards.addAll(cards);
        notifyDataSetChanged();
    }
}
