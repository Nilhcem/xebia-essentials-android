package com.nilhcem.xebia.essentials.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;
import com.nilhcem.xebia.essentials.model.Category;

@EBean(scope = Scope.Singleton)
public class InMemoryCategoryFinder {
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

	public Category getById(Long id) {
		return mCategories.get(id);
	}
}
