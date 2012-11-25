package com.nilhcem.xebia.essentials.menudrawer;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.model.Category;

@EViewGroup(R.layout.menudrawer_list_item)
public class MenuDrawerListItemView extends RelativeLayout {
	@ViewById(R.id.menudrawerItemTitle)
	protected TextView mTitle;

	@ViewById(R.id.menudrawerItemIcon)
	protected ImageView mIcon;

	public MenuDrawerListItemView(Context context) {
		super(context);
	}

	public void bind(Category category) {
		long id = category.getId();
		mTitle.setText(category.getName());

		if (id == Category.CATEGORY_ID_SCAN) {
			mIcon.setBackgroundResource(R.drawable.ic_qrcode);
		} else if (id == Category.CATEGORY_ID_SEARCH) {
			mIcon.setBackgroundResource(R.drawable.ic_search);
		} else {
			float[] outerRadii = new float[] { 6, 6, 6, 6, 6, 6, 6, 6 };
			RoundRectShape rect = new RoundRectShape(outerRadii, null, null);
			ShapeDrawable bg = new ShapeDrawable(rect);
			bg.getPaint().setColor(category.getIntColor());
			Compatibility.setDrawableToView(mIcon, bg);
		}
	}
}
