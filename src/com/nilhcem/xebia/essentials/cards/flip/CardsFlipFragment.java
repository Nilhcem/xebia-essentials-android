package com.nilhcem.xebia.essentials.cards.flip;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.googlecode.androidannotations.annotations.res.ColorRes;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.AbstractCardFragment;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.qrcode.QRCodeGenerator;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;
import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

@EFragment(R.layout.cards_flip_fragment)
public class CardsFlipFragment extends AbstractCardFragment {
	private static final int DEFAULT_FLIP_DURATION = 500;

	@ViewById(R.id.cardsFlipViewFlipper)
	protected ViewAnimator mViewAnimator;

	@ViewById(R.id.cardsFlipLayout1)
	protected RelativeLayout mCardSide1;

	@ViewById(R.id.cardsFlipLayout2)
	protected RelativeLayout mCardSide2;

	@ViewById(R.id.cardsFlipQrCodeLayout)
	protected FrameLayout mQrCodeLayout;

	@ViewById(R.id.cardsFlipQrCodeLayoutProgress)
	protected ProgressBar mQrCodeProgressBar;

	@ViewById(R.id.cardsFlipCategory1)
	protected TextView mCategorySide1;

	@ViewById(R.id.cardsFlipCategory2)
	protected TextView mCategorySide2;

	@ViewById(R.id.cardsFlipTitle)
	protected TextView mTitle;

	@ViewById(R.id.cardsFlipSummary)
	protected TextView mSummary;

	@ViewById(R.id.cardsFlipUrl)
	protected TextView mUrl;

	@ViewById(R.id.cardsFlipQrCode)
	protected ImageView mQrCode;

	@ColorRes(R.color.white)
	protected int mWhite;

	@BooleanRes(R.bool.multipaned)
	protected boolean mIsMultipaned;

	@StringRes(R.string.bitly_url_prefix)
	protected String mBitlyPrefix;

	@Bean
	protected QRCodeGenerator mQRCodeGenerator;

	private SharedPreferences mPrefs;
	private Activity mActivity;
	private boolean mAnimateSwap;
	private int mScreenWidth;
	private int mScreenHeight;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;
		Point dimensions = Compatibility.getScreenDimensions(activity);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		refreshSwapBoolean();

		mScreenWidth = dimensions.x;
		mScreenHeight = dimensions.y;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@AfterViews
	protected void initCardSide() {
		if (mCache.isCardFlipOnBackSide(mCard)) {
			flipCard(false);
		}
	}

	@AfterViews
	protected void initCardsFlipAnimator() {
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				flipCard(mAnimateSwap);
				mCache.setCardFlipSide(mCard);
			}
		};
		mViewAnimator.setOnClickListener(listener);
		mSummary.setOnClickListener(listener);
		mTitle.setOnClickListener(listener);
	}

	@AfterViews
	protected void initCardData() {
		// Set card size programmatically, depending on the device's screen
		FrameLayout.LayoutParams params = getCardsLayoutParams();
		mCardSide1.setLayoutParams(params);
		mCardSide2.setLayoutParams(params);
		int sidePaddingWidth = setCardPadding(params);

		Category category = mCache.getCategoryById(mCard.getCategoryId());
		if (category != null) {
			mCategorySide1.setText(category.getName());
			mCategorySide2.setText(category.getName());

			Drawable bg = getCardBackground(category.getIntColor(), sidePaddingWidth);
			Compatibility.setDrawableToView(mCardSide1, bg);
			Compatibility.setDrawableToView(mCardSide2, bg);
		}

		mTitle.setText(mCard.getTitle());
		mUrl.setText(mCard.getUrl());
		mSummary.setText(Html.fromHtml(mCard.getSummary()));

		// Set QR code size - Adjust depending on screen size
		int qrCodeSize = Math.round((float) params.height / 3.75f);
		mQrCodeLayout.getLayoutParams().width = qrCodeSize;
		mQrCodeLayout.getLayoutParams().height = qrCodeSize;
		generateQrCode(qrCodeSize);

		// Set title size
		float titleRatio = (Compatibility.convertPixelsToDp(params.width, mActivity) < 350) ? 7f : 6f;
		float titleSize = (params.height - qrCodeSize) / titleRatio;
		float minTextSize = Compatibility.convertDpToPixel(22f, mActivity);
		if (titleSize < minTextSize) {
			titleSize = minTextSize;
		}
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);

		// Set summary size
		float summarySize = params.height / 16;
		float summaryMinSize = Compatibility.convertDpToPixel(16f, mActivity);
		if (summarySize < summaryMinSize) {
			summarySize = summaryMinSize;
		}
		mSummary.setTextSize(TypedValue.COMPLEX_UNIT_PX, summarySize);
	}

	@Background
	protected void generateQrCode(int size) {
		int qrCodeSize = size * 3; // otherwise QR code is too small
		String toConvert = String.format("%s%s", mBitlyPrefix, mCard.getBitly());
		Bitmap qrCode = mQRCodeGenerator.generate(toConvert, qrCodeSize);
		if (qrCode != null) {
			setQrCode(qrCode);
		}
	}

	@UiThread
	protected void setQrCode(Bitmap bitmap) {
		mQrCode.setImageBitmap(bitmap);
		mQrCodeProgressBar.setVisibility(View.GONE);
	}

	private void flipCard(boolean animate) {
		int duration = (animate) ? DEFAULT_FLIP_DURATION : 0;
		AnimationFactory.flipTransition(mViewAnimator,
				FlipDirection.LEFT_RIGHT, duration);
	}

	private int setCardPadding(FrameLayout.LayoutParams params) {
		// Compute side width depending on device's screen size (12dp or 10dp if width is too small)
		float borderDp;
		float bottomDp;
		float sideDp;
		if (Compatibility.convertPixelsToDp(params.height, mActivity) < 280) {
			borderDp = 10f;
			bottomDp = 12f;
			sideDp = 8f;
		} else {
			borderDp = 13f;
			bottomDp = 14f;
			sideDp = 16f;
		}
		int borderPx = Math.round(Compatibility.convertDpToPixel(borderDp, mActivity));
		int bottomPx = borderPx + Math.round(Compatibility.convertDpToPixel(bottomDp, mActivity));
		int sidePx = borderPx + Math.round(Compatibility.convertDpToPixel(sideDp, mActivity));

		mCardSide1.setPadding(sidePx, borderPx, sidePx, bottomPx);
		mCardSide2.setPadding(sidePx, borderPx, sidePx, bottomPx);
		return borderPx;
	}

	private Drawable getCardBackground(int color, int sidePaddingWidth) {
		float r = 2f; // radius

		GradientDrawable g = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] { color, color });
		g.setGradientType(GradientDrawable.RECTANGLE);
		g.setStroke(sidePaddingWidth, mWhite);
		g.setCornerRadii(new float[] { r, r, r, r, r, r, r, r });
		return g;
	}

	private FrameLayout.LayoutParams getCardsLayoutParams() {
		int width = mScreenWidth;
		int height = mScreenHeight;

		// If multipaned, then width must be 35% less
		if (mIsMultipaned) {
			width = width * 65 / 100;
		}

		// Add some padding
		width = width * 86 / 100;
		height = height * 70 / 100;

		// Add some more padding for large devices
		if (Compatibility.convertPixelsToDp(width, mActivity) > 560) {
			width = width * 82 / 100;
		}

		// Ratio: width: 1, height: 0.65 (or 0.8 for very small screen devices)
		int heightRatio = (Compatibility.convertPixelsToDp(width, mActivity) < 300) ? 80 : 65;
		int newHeight = width * heightRatio / 100;
		if (newHeight < height) {
			height = newHeight;
		}

		// Readjust width if too large compared to height
		int adjustedWidth = Math.round(1.75f * height);
		if (width > adjustedWidth) {
			width = adjustedWidth;
		}

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
		params.gravity = Gravity.CENTER;
		return params;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshSwapBoolean();
	}

	private void refreshSwapBoolean() {
		if (mPrefs != null) {
			mAnimateSwap = mPrefs.getBoolean(SettingsActivity.KEY_ANIMATION, true);
		}
	}
}
