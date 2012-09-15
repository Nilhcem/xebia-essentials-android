package com.nilhcem.xebia.essentials.dashboard;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.model.Category;

@EViewGroup(R.layout.dashboard_list_item)
public class DashboardListItemView extends LinearLayout {
	@ViewById(R.id.dashboardItemTitle)
	protected TextView mTitle;

	@ViewById(R.id.dashboardItemColor)
	protected View mView;

	public DashboardListItemView(Context context) {
		super(context);
	}

	public void bind(Category category) {
		mTitle.setText(category.getName());

		RoundRectShape rect = new RoundRectShape(new float[] { 6, 6, 6, 6, 6,
				6, 6, 6 }, null, null);
		ShapeDrawable bg = new ShapeDrawable(rect);
		bg.getPaint().setColor(category.getIntColor());
		Compatibility.setDrawableToView(mView, bg);
	}
}
