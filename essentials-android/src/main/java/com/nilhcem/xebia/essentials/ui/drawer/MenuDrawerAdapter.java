package com.nilhcem.xebia.essentials.ui.drawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CategoriesDao;
import com.nilhcem.xebia.essentials.model.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MenuDrawerAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Object> mItems;

    @Inject DataProvider mDataProvider;
    @Inject CategoriesDao mCategoriesDao;

    public MenuDrawerAdapter(Context context) {
        EssentialsApplication.get(context).inject(this);

        mContext = context;
        List items = new ArrayList();
        List<Category> categories = mCategoriesDao.getCategories();

        // CATEGORIES
        items.add(context.getString(R.string.drawer_header_categories));
        items.add(new Category(Category.CATEGORY_ID_ALL, context.getString(R.string.drawer_section_all),
                context.getResources().getColor(R.color.xebia_purple)));
        for (Category category : categories) {
            items.add(category);
        }

        // OTHER
        items.add(context.getString(R.string.drawer_header_other));
        items.add(new Category(Category.CATEGORY_ID_SEARCH, context.getString(R.string.drawer_section_search), 0));
        items.add(new Category(Category.CATEGORY_ID_SCAN, context.getString(R.string.drawer_section_scan), 0));
        items.add(new Category(Category.CATEGORY_ID_ABOUT, context.getString(R.string.drawer_section_about), 0));

        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        Object obj = getItem(position);
        if (obj instanceof Category) {
            return ((Category) obj).getIntId();
        }
        // adding mItems.size() to avoid collisions with real categories ids
        return (long) mItems.size() + position;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        if (obj instanceof String) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof Category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);
        View view = null;

        if (item instanceof String) {
            view = getHeaderView(convertView, (String) item, position != 0);
        } else if (item instanceof Category) {
            view = getCategoryView(convertView, (Category) item);
        }
        return view;
    }

    private View getHeaderView(View convertView, String header, boolean addTopPadding) {
        MenuDrawerEntryHeaderView headerView;

        if (convertView == null) {
            headerView = new MenuDrawerEntryHeaderView(mContext);
        } else {
            headerView = (MenuDrawerEntryHeaderView) convertView;
        }
        headerView.bindData(header, addTopPadding);
        return headerView;
    }

    private View getCategoryView(View convertView, Category category) {
        MenuDrawerEntryItemView itemView;

        if (convertView == null) {
            itemView = new MenuDrawerEntryItemView(mContext);
        } else {
            itemView = (MenuDrawerEntryItemView) convertView;
        }
        itemView.bindData(category, mDataProvider.getCurrentCategoryId() == category.getIntId());
        return itemView;
    }
}
