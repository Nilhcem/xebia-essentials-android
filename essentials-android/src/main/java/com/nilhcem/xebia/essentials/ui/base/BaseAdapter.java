package com.nilhcem.xebia.essentials.ui.base;

import android.content.Context;

import java.util.Collections;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected final Context mContext;
    protected List<T> mItems = Collections.emptyList();

    protected BaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
