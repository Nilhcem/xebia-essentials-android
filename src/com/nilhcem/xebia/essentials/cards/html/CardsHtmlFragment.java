package com.nilhcem.xebia.essentials.cards.html;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.AbstractCardFragment;
import com.nilhcem.xebia.essentials.core.model.Category;

@EFragment(R.layout.cards_html_fragment)
public class CardsHtmlFragment extends AbstractCardFragment {

	@ViewById(R.id.cardsHtmlScroll)
	protected ScrollView mScroll;

	@ViewById(R.id.cardsHtmlTitle)
	protected TextView mTitle;

	@ViewById(R.id.cardsHtmlCategory)
	protected TextView mCategory;

	@ViewById(R.id.cardsHtmlContent)
	protected TextView mContent;

	@AfterViews
	protected void initCardData() {
		Category category = mCache.getCategoryById(mCard.getCategoryId());
		if (category != null) {
			mTitle.setBackgroundColor(category.getIntColor());
			mCategory.setText(category.getName());
			mCategory.setBackgroundColor(category.getIntColor());
		}

		mTitle.setText(mCard.getTitle());

		String content = mCard.getContent();
		if (TextUtils.isEmpty(content)) {
			content = mCard.getSummary();
		}
		if (content == null) {
			content = "";
		}

		mContent.setText(Html.fromHtml(content));
		mContent.setMovementMethod(LinkMovementMethod.getInstance()); // make links clickable
	}
}
