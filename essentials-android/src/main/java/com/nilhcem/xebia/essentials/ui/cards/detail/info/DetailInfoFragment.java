package com.nilhcem.xebia.essentials.ui.cards.detail.info;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;
import com.nilhcem.xebia.essentials.ui.cards.detail.BaseDetailFragment;

import butterknife.Bind;

import static android.os.Build.VERSION_CODES.HONEYCOMB;

public class DetailInfoFragment extends BaseDetailFragment {

    @Bind(R.id.detail_info_title) TextView mTitle;
    @Bind(R.id.detail_info_category) TextView mCategory;
    @Bind(R.id.detail_info_text) TextView mDescription;

    public DetailInfoFragment() {
        super(R.layout.detail_info_fragment);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void updateContent(Card card) {
        Category category = card.getCategory();

        int categoryColor = category.getColor();
        mTitle.setBackgroundColor(categoryColor);
        mCategory.setBackgroundColor(categoryColor);
        mCategory.setText(category.getName());

        mTitle.setText(card.getTitle());
        mDescription.setText(Html.fromHtml(card.getDescription()));

        Linkify.addLinks(mDescription, Linkify.WEB_URLS);
        if (Compatibility.isCompatible(HONEYCOMB)) {
            mDescription.setTextIsSelectable(true);
        }
        mDescription.setMovementMethod(LinkMovementMethod.getInstance());
        mDescription.setLinkTextColor(categoryColor);
    }
}
