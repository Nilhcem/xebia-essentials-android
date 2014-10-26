package com.nilhcem.xebia.essentials.ui.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CategoriesDao;
import com.nilhcem.xebia.essentials.core.utils.ColorUtils;
import com.nilhcem.xebia.essentials.model.Category;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends ActionBarActivity {

    @Inject protected EventBus mEventBus;
    @Inject protected DataProvider mDataProvider;
    @Inject CategoriesDao mCategoriesDao;

    private final boolean mUseEventBus;
    private final int mLayoutResId;

    protected BaseActivity(int layoutResId, boolean useEventBus) {
        mLayoutResId = layoutResId;
        mUseEventBus = useEventBus;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EssentialsApplication.get(this).inject(this);

        if (mLayoutResId != 0) {
            setContentView(R.layout.menudrawer_layout);
            ButterKnife.inject(this);
        }
        updateActionBarColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUseEventBus) {
            mEventBus.register(this);
        }
    }

    @Override
    protected void onPause() {
        if (mUseEventBus) {
            mEventBus.unregister(this);
        }
        super.onPause();
    }

    protected void updateActionBarColor() {
        final int color;
        Category category = mCategoriesDao.getCategoryById(mDataProvider.getCurrentCategoryId());
        if (category == null) {
            color = getResources().getColor(R.color.actionbar_color);
        } else {
            color = category.getColor();
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ColorUtils.darker(color, 0.9f)));
    }
}
