package com.nilhcem.xebia.essentials.ui.cards.detail.card;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.utils.Compatibility;
import com.nilhcem.xebia.essentials.model.Card;
import com.nilhcem.xebia.essentials.model.Category;
import com.nilhcem.xebia.essentials.ui.cards.detail.BaseDetailFragment;

import java.util.Locale;

import butterknife.InjectView;
import timber.log.Timber;

public class DetailCardFragment extends BaseDetailFragment implements View.OnClickListener, DetailCardFlipperAnimator.OnCardFlipListener {

    @InjectView(R.id.detail_card_container) ViewGroup mCardContainer;
    @InjectView(R.id.detail_card_category) TextView mCardCategory;
    @InjectView(R.id.detail_card_front) View mCardFront;
    @InjectView(R.id.detail_card_back) View mCardBack;

    @InjectView(R.id.detail_card_front_title) TextView mCardTitle;
    @InjectView(R.id.detail_card_front_url) TextView mCardUrl;
    @InjectView(R.id.detail_card_back_summary) TextView mCardSummary;

    @InjectView(R.id.detail_card_front_qrcode) DetailCardQrCodeView mQrCode;

    private View mLayout;
    private int mCardBorderSize;
    private DetailCardFlipperAnimator mAnimator;

    public DetailCardFragment() {
        super(R.layout.detail_card_fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCardBorderSize = activity.getResources().getDimensionPixelSize(R.dimen.card_flip_white_border);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = super.onCreateView(inflater, container, savedInstanceState);
        setCardSize();

        mLayout.setOnClickListener(this);
        mCardTitle.setOnClickListener(this);
        mCardSummary.setOnClickListener(this);

        mAnimator = new DetailCardFlipperAnimator(mCardContainer, this);
        return mLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAnimator.onSaveInstanceState(outState, mCard.getUrlId());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mAnimator.onViewStateRestored(savedInstanceState, mCard.getUrlId());
    }

    @Override
    protected void updateContent(Card card) {
        Category category = card.getCategory();

        int categoryColor = category.getColor();
        setCardBackground(categoryColor);
        mCardCategory.setText(category.getName());

        // Front
        mCardTitle.setText(card.getTitle());
        String url = String.format(Locale.US, "http://essentials.xebia.com/%s", card.getUrlId());
        mCardUrl.setText(url);
        mQrCode.bindData(url);

        // Back
        String summary = card.getSummary();
        if (summary == null) {
            summary = "";
        }
        mCardSummary.setText(Html.fromHtml(summary));
    }

    /**
     * Sets card size programmatically, according to the fragment size
     */
    private void setCardSize() {
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mLayout.getWidth();
                int height = mLayout.getHeight();

                if (width > 0 && height > 0) {
                    Point cardSize = computeCardSize(width, height, getActivity());

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(cardSize.x, cardSize.y);
                    params.gravity = Gravity.CENTER;
                    mCardContainer.setLayoutParams(params);

                    setCardPadding(cardSize);

                    Compatibility.removeOnGlobalLayoutListener(mLayout.getViewTreeObserver(), this);
                }
            }
        });
    }

    private Point computeCardSize(int availableWidth, int availableHeight, Context context) {
        // Add some padding
        int outerPaddingRatio = 94;
        int width = availableWidth * outerPaddingRatio / 100;
        int height = availableHeight * outerPaddingRatio / 100;

        // Compute card width ratio (large devices: 70% - small devices: 75%)
        int ratio = (Compatibility.convertDpToIntPixel(width, context) < 340) ? 75 : 70;
        boolean isInPortrait = height > width;
        if (isInPortrait) {
            height = Math.min(height, width * ratio / 100);
        } else {
            width = Math.min(width, height * 100 / ratio);
        }

        // Set the card's max size (otherwise it doesn't look that great on large devices)
        int maxWidth = Compatibility.convertDpToIntPixel(520f, getActivity());
        if (width > maxWidth) {
            width = maxWidth;
            height = maxWidth * ratio / 100;
        }

        return new Point(width, height);
    }

    /**
     * Sets padding programmatically according to card size
     */
    private void setCardPadding(Point cardSize) {
        int paddingVerticalPx = mCardBorderSize + Math.round(1.47f * cardSize.y / 100);
        int paddingHorizontalPx = mCardBorderSize + Math.round(2.08f * cardSize.x / 100);
        mCardContainer.setPadding(paddingHorizontalPx, paddingVerticalPx, paddingHorizontalPx, paddingVerticalPx);
    }

    private void setCardBackground(int color) {
        Context context = getActivity();
        float radius = Compatibility.convertDpToPixel(2f, context);
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{color, color});
        bg.setGradientType(GradientDrawable.RECTANGLE);
        bg.setStroke(mCardBorderSize, getResources().getColor(R.color.xebia_white));
        bg.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        Compatibility.setBackground(mCardContainer, bg);
        Compatibility.setElevation(mCardContainer, Compatibility.convertDpToPixel(40f, getActivity()));
    }

    @Override
    public void onClick(View v) {
        Timber.d("Flip card");
        mAnimator.startFlipping();
    }

    @Override
    public void onCardFlip(boolean isFrontSideShowing) {
        if (isFrontSideShowing) {
            mCardBack.setVisibility(View.GONE);
            mCardFront.setVisibility(View.VISIBLE);
        } else {
            mCardFront.setVisibility(View.GONE);
            mCardBack.setVisibility(View.VISIBLE);
        }
    }
}
