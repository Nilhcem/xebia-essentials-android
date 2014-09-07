package com.nilhcem.xebia.essentials.ui.cards.listing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListingEntryView extends RelativeLayout {

    @InjectView(R.id.listing_text) TextView mText;
    @InjectView(R.id.listing_category_color) View mCategoryColor;

    public ListingEntryView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.listing_entry, this, true);
        ButterKnife.inject(this, this);
        EssentialsApplication.get(context).inject(this);
    }

    public void bindData(Card card, boolean highlightItem) {
        Category category = card.getCategory();

        mText.setText(card.getTitle());
        if (highlightItem) {
            mText.setTextColor(getResources().getColor(R.color.xebia_white));
            mText.setBackgroundColor(category.getColor());
        } else {
            mText.setTextColor(getResources().getColor(R.color.xebia_dark));
            Compatibility.setBackground(mText, null);
        }

        mCategoryColor.setBackgroundColor(category.getColor());
    }
}
