package com.nilhcem.xebia.essentials.menudrawer;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;

@EViewGroup(R.layout.menudrawer_list_category)
public class MenuDrawerListCategoryView extends LinearLayout {
	@ViewById(R.id.menudrawerCategoryTitle)
	protected TextView mTitle;

	public MenuDrawerListCategoryView(Context context) {
		super(context);
	}

	public void bind(String title) {
		mTitle.setText(title);
	}
}
