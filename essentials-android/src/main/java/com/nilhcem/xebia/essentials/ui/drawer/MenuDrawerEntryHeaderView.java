package com.nilhcem.xebia.essentials.ui.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuDrawerEntryHeaderView extends LinearLayout {

    @Bind(R.id.drawer_entry_header) TextView mText;

    private final int mSmallTopMargin;
    private final int mLargeTopMargin;

    public MenuDrawerEntryHeaderView(Context context) {
        super(context);

        mSmallTopMargin = Compatibility.convertDpToIntPixel(2f, context);
        mLargeTopMargin = Compatibility.convertDpToIntPixel(12f, context);

        LayoutInflater.from(context).inflate(R.layout.menudrawer_entry_header, this, true);
        ButterKnife.bind(this, this);
    }

    @Override
    protected void finalize() throws Throwable {
        ButterKnife.unbind(this);
        super.finalize();
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
