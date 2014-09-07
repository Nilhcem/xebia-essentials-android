package com.nilhcem.xebia.essentials.ui.cards.detail.card;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nilhcem.xebia.essentials.R;

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

class DetailCardFlipperAnimator implements Animation.AnimationListener {

    private static final String EXTRA_IS_FRONT_SIDE_SHOWING = "mIsFrontSideShowing";
    private static final String EXTRA_CARD_ID = "mCardId";

    private final Animation mCardAnimationToMiddle;
    private final Animation mCardAnimationFromMiddle;

    private final View mViewToAnimate;
    private final OnCardFlipListener mOnCardFlipListener;

    private final AtomicBoolean mIsAnimating = new AtomicBoolean(false);

    private boolean mIsFrontSideShowing = true;

    public DetailCardFlipperAnimator(View viewToAnimate, OnCardFlipListener onCardFlipListener) {
        mViewToAnimate = viewToAnimate;
        mOnCardFlipListener = onCardFlipListener;
        Context context = viewToAnimate.getContext();

        mCardAnimationToMiddle = AnimationUtils.loadAnimation(context, R.anim.to_middle);
        mCardAnimationToMiddle.setAnimationListener(this);
        mCardAnimationFromMiddle = AnimationUtils.loadAnimation(context, R.anim.from_middle);
        mCardAnimationFromMiddle.setAnimationListener(this);
    }

    public void onSaveInstanceState(Bundle outState, String cardId) {
        outState.putBoolean(EXTRA_IS_FRONT_SIDE_SHOWING, mIsFrontSideShowing);
        outState.putString(EXTRA_CARD_ID, cardId);
    }

    public void onViewStateRestored(Bundle savedInstanceState, String cardId) {
        if (savedInstanceState != null) {
            String savedCardId = savedInstanceState.getString(EXTRA_CARD_ID);
            if (cardId.equals(savedCardId) && !savedInstanceState.getBoolean(EXTRA_IS_FRONT_SIDE_SHOWING, true)) {
                flipSilently();
            }
        }
    }

    public void startFlipping() {
        if (mIsAnimating.compareAndSet(false, true)) {
            mViewToAnimate.clearAnimation();
            mViewToAnimate.setAnimation(mCardAnimationToMiddle);
            mViewToAnimate.startAnimation(mCardAnimationToMiddle);
        } else {
            Timber.w("Already animating");
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Do nothing
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == mCardAnimationToMiddle) {
            mIsFrontSideShowing = !mIsFrontSideShowing;
            mOnCardFlipListener.onCardFlip(mIsFrontSideShowing);

            mViewToAnimate.clearAnimation();
            mViewToAnimate.setAnimation(mCardAnimationFromMiddle);
            mViewToAnimate.startAnimation(mCardAnimationFromMiddle);
        } else {
            mIsAnimating.set(false);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Do nothing
    }

    private void flipSilently() {
        mIsFrontSideShowing = !mIsFrontSideShowing;
        mOnCardFlipListener.onCardFlip(mIsFrontSideShowing);
    }

    public static interface OnCardFlipListener {
        void onCardFlip(boolean isFrontSideShowing);
    }
}
