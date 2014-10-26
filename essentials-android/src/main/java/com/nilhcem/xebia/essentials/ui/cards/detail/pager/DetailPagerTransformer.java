package com.nilhcem.xebia.essentials.ui.cards.detail.pager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nilhcem.xebia.essentials.core.data.provider.DataProvider;

import javax.inject.Inject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailPagerTransformer implements ViewPager.PageTransformer {

    private static final float ROT_MOD = -14f;

    @Inject DataProvider mDataProvider;

    private boolean mEnableTransformations = true;
    private OnPageTransformedListener mPageTransformedListener;

    public interface OnPageTransformedListener {
        void onPageTransformed(View view, float position);
    }

    /**
     * Apply a property transformation to the given page. For most use cases, this method should not be overridden.
     * Instead use {@link #transformPage(View, float)} to perform typical transformations.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    @Override
    public void transformPage(View page, float position) {
        if (mDataProvider.shouldDisplayCardView()) {
            onPreTransform(page);
            if (mEnableTransformations) {
                onTransform(page, position);
            }
        }
    }

    /**
     * Called each {@link #transformPage(View, float)}.
     *
     * @param view     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    private void onTransform(View view, float position) {
        final float width = view.getWidth();
        final float height = view.getHeight();
        final float rotation = ROT_MOD * position * -1.25f;

        view.setPivotX(width * 0.5f);
        view.setPivotY(height);
        view.setRotation(rotation);

        if (mPageTransformedListener != null) {
            mPageTransformedListener.onPageTransformed(view, position);
        }
    }

    /**
     * Called each {@link #transformPage(View, float)} before {{@link #onTransform(View, float)}.
     * <p/>
     * The default implementation attempts to reset all view properties. This is useful when toggling transforms that do
     * not modify the same page properties. For instance changing from a transformation that applies rotation to a
     * transformation that fades can inadvertently leave a fragment stuck with a rotation or with some degree of applied
     * alpha.
     *
     * @param page Apply the transformation to this page
     */
    protected void onPreTransform(View page) {
        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationY(0);
        page.setTranslationX(0);
        page.setAlpha(1);
    }

    public void setEnableTransformations(boolean enableTransformations) {
        mEnableTransformations = enableTransformations;
    }

    public void setPageTransformedListener(OnPageTransformedListener listener) {
        mPageTransformedListener = listener;
    }
}
