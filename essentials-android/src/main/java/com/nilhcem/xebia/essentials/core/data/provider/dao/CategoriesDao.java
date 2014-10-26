package com.nilhcem.xebia.essentials.core.data.provider.dao;

import com.activeandroid.query.Select;
import com.nilhcem.xebia.essentials.model.Category;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CategoriesDao {

    @Inject
    public CategoriesDao() {
    }

    public Category getCategoryById(int id) {
        return new Select().from(Category.class).where("id = ?", id).executeSingle();
    }

    public List<Category> getCategories() {
        return new Select().from(Category.class).orderBy("name ASC").execute();
    }
}
