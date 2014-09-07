package com.nilhcem.xebia.essentials.ui.cards.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.ui.base.BaseFragment;
import com.nilhcem.xebia.essentials.ui.cards.detail.card.DetailCardFragment;
import com.nilhcem.xebia.essentials.ui.cards.detail.info.DetailInfoFragment;

public abstract class BaseDetailFragment extends BaseFragment {

    private static final String ARG_CARD = "mCard";

    protected Card mCard;

    protected BaseDetailFragment(int layoutResToInflate) {
        super(layoutResToInflate, false);
    }

    public static BaseDetailFragment newInstance(Card card, boolean cardView) {
        BaseDetailFragment fragment;

        if (cardView) {
            fragment = new DetailCardFragment();
        } else {
            fragment = new DetailInfoFragment();
        }

        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mCard = arguments.getParcelable(ARG_CARD);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent(mCard);
    }

    protected abstract void updateContent(Card card);

    public Card getCard() {
        return mCard;
    }
}
