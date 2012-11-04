package com.nilhcem.xebia.essentials.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.nilhcem.xebia.essentials.core.model.Category;

@EBean(scope = Scope.Singleton)
public class InMemoryCache {
	private long mSelectedCategory = Category.ALL_CATEGORIES_ID;
	private int mCardPosition = CARD_POSITION_UNSET; // useful when orientation changed on CardsHtmlActivity, switching from one to two panes
	private Map<Long, Category> mCategories;

	private static int CARD_POSITION_UNSET = -1;

	public void initCategories(List<Category> categories) {
		mCategories = new HashMap<Long, Category>();
		for (Category category : categories) {
			mCategories.put(category.getId(), category);
		}
	}

	public List<Category> getAllCategories() {
		if (mCategories == null) {
			return null;
		}
		return new ArrayList<Category>(mCategories.values());
	}

	public Category getCategoryById(long id) {
		if (mCategories == null) {
			return null;
		}
		return mCategories.get(Long.valueOf(id));
	}

	public long getSelectedCategory() {
		return mSelectedCategory;
	}

	public void setSelectedCategory(long categoryId) {
		mSelectedCategory = categoryId;
		resetCardPosition();
	}

	public int getCardPosition() {
		return mCardPosition;
	}

	public void setCardPosition(int position) {
		mCardPosition = position;
	}

	public boolean isCardPositionSet() {
		return mCardPosition != CARD_POSITION_UNSET;
	}

	public void resetCardPosition() {
		mCardPosition = CARD_POSITION_UNSET;
	}

	public boolean isInitialized() {
		return (mCategories != null && mCategories.size() > 0);
	}
}
