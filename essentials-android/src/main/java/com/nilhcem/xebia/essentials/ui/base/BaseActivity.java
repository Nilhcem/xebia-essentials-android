package com.nilhcem.xebia.essentials.ui.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CategoriesDao;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.nilhcem.xebia.essentials.core.utils.ColorUtils.darker;

public abstract class BaseActivity extends AppCompatActivity {

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
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EssentialsApplication.get(this).inject(this);

        if (mLayoutResId != 0) {
            setContentView(R.layout.menudrawer_layout);
            ButterKnife.bind(this);
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

    @Override
    protected void onDestroy() {
        if (mLayoutResId != 0) {
            ButterKnife.unbind(this);
        }
        super.onDestroy();
    }

    public void updateActionBarColor() {
        updateActionBarColor(mCategoriesDao.getCategoryColor(mDataProvider.getCurrentCategoryId(), getResources()));
    }

    public void updateActionBarColor(int color) {
        int actionBarColor = darker(color, 0.8f);
        int statusBarColor = darker(color, 0.65f);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));
        Compatibility.setStatusBarColor(this, statusBarColor);
    }
}
