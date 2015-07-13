package com.nilhcem.xebia.essentials.ui.drawer;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;
import com.nilhcem.xebia.essentials.model.Category;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuDrawerEntryItemView extends RelativeLayout {

    @Bind(R.id.drawer_entry_icon) ImageView mIcon;
    @Bind(R.id.drawer_entry_title) TextView mTitle;

    private final float[] mOuterRadii;

    public MenuDrawerEntryItemView(Context context) {
        super(context);
        float f = Compatibility.convertDpToPixel(5f, context);
        mOuterRadii = new float[]{f, f, f, f, f, f, f, f};

        LayoutInflater.from(context).inflate(R.layout.menudrawer_entry_item, this, true);
        ButterKnife.bind(this, this);
    }

    @Override
    protected void finalize() throws Throwable {
        ButterKnife.unbind(this);
        super.finalize();
    }

    public void bindData(Category category, boolean highlight) {
        int id = category.getIntId();
        mTitle.setText(category.getName());

        if (id == Category.CATEGORY_ID_SCAN) {
            Compatibility.setBackground(mIcon, new IconDrawable(getContext(), Iconify.IconValue.fa_qrcode)
                    .colorRes(R.color.drawer_icon)
                    .sizeRes(R.dimen.menu_drawer_icon));
        } else if (id == Category.CATEGORY_ID_SEARCH) {
            Compatibility.setBackground(mIcon, new IconDrawable(getContext(), Iconify.IconValue.fa_search)
                    .colorRes(R.color.drawer_icon)
                    .sizeRes(R.dimen.menu_drawer_icon));
        } else if (id == Category.CATEGORY_ID_ABOUT) {
            Compatibility.setBackground(mIcon, new IconDrawable(getContext(), Iconify.IconValue.fa_quote_left)
                    .colorRes(R.color.drawer_icon)
                    .sizeRes(R.dimen.menu_drawer_icon));
        } else {
            RoundRectShape rect = new RoundRectShape(mOuterRadii, null, null);
            ShapeDrawable bg = new ShapeDrawable(rect);
            bg.getPaint().setColor(category.getColor());
            Compatibility.setBackground(mIcon, bg);
        }

        if (highlight) {
            setBackgroundColor(category.getColor() - 0xdf000000);
        } else {
            Compatibility.setBackground(this, null);
        }
    }
}
