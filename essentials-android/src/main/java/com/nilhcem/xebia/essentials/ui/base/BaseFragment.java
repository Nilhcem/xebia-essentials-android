package com.nilhcem.xebia.essentials.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nilhcem.xebia.essentials.EssentialsApplication;
import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public abstract class BaseFragment extends Fragment {

    @Inject protected EventBus mEventBus;
    @Inject protected DataProvider mDataProvider;

    private final boolean mUseEventBus;
    private final int mLayoutResId;

    protected BaseFragment(int layoutResId, boolean useEventBus) {
        mLayoutResId = layoutResId;
        mUseEventBus = useEventBus;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EssentialsApplication.get(getActivity()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (mLayoutResId != 0) {
            view = inflater.inflate(mLayoutResId, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUseEventBus) {
            mEventBus.register(this);
        }
    }

    @Override
    public void onPause() {
        if (mUseEventBus) {
            mEventBus.unregister(this);
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mLayoutResId != 0) {
            ButterKnife.unbind(this);
        }
        super.onDestroyView();
    }
}
