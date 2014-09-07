package com.nilhcem.xebia.essentials.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends ActionBarActivity {

    @Inject protected EventBus mEventBus;
    @Inject protected DataProvider mDataProvider;

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
}
