package com.nilhcem.xebia.essentials.core.data.provider.dao;

import android.content.res.Resources;

import com.activeandroid.query.Select;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.model.Category;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CategoriesDao {

    @Inject
    public CategoriesDao() {
    }

    public int getCategoryColor(int categoryId, Resources res) {
        final int color;
        Category category = getCategoryById(categoryId);
        if (category == null) {
            color = res.getColor(R.color.actionbar_color);
        } else {
            color = category.getColor();
        }
        return color;
    }

    public List<Category> getCategories() {
        return new Select().from(Category.class).orderBy("name ASC").execute();
    }

    private Category getCategoryById(int id) {
        return new Select().from(Category.class).where("id = ?", id).executeSingle();
    }
}
