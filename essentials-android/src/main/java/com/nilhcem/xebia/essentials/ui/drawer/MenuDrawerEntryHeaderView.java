package com.nilhcem.xebia.essentials.ui.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MenuDrawerEntryHeaderView extends LinearLayout {

    @InjectView(R.id.drawer_entry_header) TextView mText;

    private final int mSmallTopMargin;
    private final int mLargeTopMargin;

    public MenuDrawerEntryHeaderView(Context context) {
        super(context);

        mSmallTopMargin = Compatibility.convertDpToIntPixel(2f, context);
        mLargeTopMargin = Compatibility.convertDpToIntPixel(12f, context);

        LayoutInflater.from(context).inflate(R.layout.menudrawer_entry_header, this, true);
        ButterKnife.inject(this, this);
    }

    public void bindData(String header, boolean withTopMargin) {
        mText.setText(header);

        int topMargin;
        if (withTopMargin) {
            topMargin = mLargeTopMargin;
        } else {
            topMargin = mSmallTopMargin;
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mText.getLayoutParams();
        layoutParams.setMargins(0, topMargin, 0, 0);
    }
}
