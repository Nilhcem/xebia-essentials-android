package com.nilhcem.xebia.essentials.cards;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.InMemoryCategoryFinder;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EViewGroup(R.layout.cards_list_item)
public class CardsListItemView extends LinearLayout {
	@Bean
	protected InMemoryCategoryFinder mCategoryFinder;

	@ViewById(R.id.cardsListItemText)
	protected TextView title;

	@ViewById(R.id.cardsListItemCategoryColor)
	protected View mCategoryColor;

	public CardsListItemView(Context context) {
		super(context);
	}

	public void bind(Card card) {
		title.setText(card.getTitle());

		// Category color
		Category category = mCategoryFinder.getById(card.getCategoryId());
		int color = Color.parseColor(category.getColor());
		mCategoryColor.setBackgroundColor(color);
	}
}
