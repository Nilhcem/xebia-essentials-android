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
	private int mCardPosition; // used when orientation changed on CardsHtmlActivity, switching from one to two panes
	private Map<Long, Category> mCategories;

	public void initCategories(List<Category> categories) {
		mCategories = new HashMap<Long, Category>();
		for (Category category : categories) {
			mCategories.put(category.getId(), category);
		}
	}

	public List<Category> getAll() {
		if (mCategories == null) {
			return null;
		}
		return new ArrayList<Category>(mCategories.values());
	}

	public Category getById(long id) {
		return mCategories.get(Long.valueOf(id));
	}

	public int getCardPosition() {
		return mCardPosition;
	}

	public void setCardPosition(int position) {
		mCardPosition = position;
	}

	public void resetCardPosition() {
		mCardPosition = 0;
	}
}
