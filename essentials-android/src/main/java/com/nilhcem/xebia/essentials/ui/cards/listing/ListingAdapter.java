package com.nilhcem.xebia.essentials.ui.cards.listing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.ui.base.BaseAdapter;

import javax.inject.Inject;

public class ListingAdapter extends BaseAdapter<Card> {

    @Inject DataProvider mDataProvider;
    private final boolean mHighlightSelectedItem;

    public ListingAdapter(Context context, boolean highlightSelectedItem) {
        super(context);
        EssentialsApplication.get(context).inject(this);
        mItems = mDataProvider.getCards();
        mHighlightSelectedItem = highlightSelectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListingEntryView view;

        if (convertView == null) {
            view = new ListingEntryView(mContext);
        } else {
            view = (ListingEntryView) convertView;
        }
        view.bindData(mItems.get(position), mHighlightSelectedItem && mDataProvider.getCurrentCardPosition(false) == position);
        return view;
    }
}
