package com.nilhcem.xebia.essentials.bo;

import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;
import com.nilhcem.xebia.essentials.dao.CategoryDao;
import com.nilhcem.xebia.essentials.model.Category;

@EBean(scope = Scope.Singleton)
public class CategoryService extends AbstractService<Category, CategoryDao> {
	@RootContext
	protected Context mContext;

	protected CategoryService() {
	}

	@AfterInject
	protected void setDao() {
		this.setDao(mContext, Category.class);
	}
}
