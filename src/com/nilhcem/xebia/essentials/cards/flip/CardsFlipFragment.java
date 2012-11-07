package com.nilhcem.xebia.essentials.cards.flip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.googlecode.androidannotations.annotations.res.ColorRes;
import com.googlecode.androidannotations.annotations.res.DimensionPixelSizeRes;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.AbstractCardFragment;
import com.nilhcem.xebia.essentials.core.Compatibility;
import com.nilhcem.xebia.essentials.core.model.Category;
import com.nilhcem.xebia.essentials.settings.SettingsActivity;
import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

@EFragment(R.layout.cards_flip_fragment)
public class CardsFlipFragment extends AbstractCardFragment {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardsFlipFragment.class);

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

	@DimensionPixelSizeRes(R.dimen.cards_flip_white_border_size)
	protected int mCardPadding;

	@ColorRes(R.color.white)
	protected int mWhite;

	@BooleanRes(R.bool.multipaned)
	protected boolean mIsMultipaned;

	@StringRes(R.string.bitly_url_prefix)
	protected String mBitlyPrefix;

	private SharedPreferences mPrefs;
	private boolean mAnimateSwap;
	private int mScreenWidth;
	private int mScreenHeight;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		Point dimensions = Compatibility.getScreenDimensions(activity);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
		refreshSwapBoolean();

		mScreenWidth = dimensions.x;
		mScreenHeight = dimensions.y;
	}

	@AfterViews
	protected void initCardsFlipAnimator() {
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAnimateSwap) {
					AnimationFactory.flipTransition(mViewAnimator,
							FlipDirection.LEFT_RIGHT);
				} else {
					int card1Visibility = mCardSide1.getVisibility();
					int card2Visibility = mCardSide2.getVisibility();
					mCardSide1.setVisibility(card2Visibility);
					mCardSide2.setVisibility(card1Visibility);
				}
			}
		};
		mViewAnimator.setOnClickListener(listener);
		mSummary.setOnClickListener(listener);
		mTitle.setOnClickListener(listener);
	}

	@AfterViews
	protected void initCardData() {
		Category category = mCache.getCategoryById(mCard.getCategoryId());
		if (category != null) {
			mCategorySide1.setText(category.getName());
			mCategorySide2.setText(category.getName());

			Drawable bg = getCardBackground(category.getIntColor());
			Compatibility.setDrawableToView(mCardSide1, bg);
			Compatibility.setDrawableToView(mCardSide2, bg);
		}

		mTitle.setText(mCard.getTitle());
		mUrl.setText(mCard.getUrl());
		mSummary.setText(Html.fromHtml(mCard.getSummary()));

		// Set card size depending on the device
		FrameLayout.LayoutParams params = getCardsLayoutParams();
		mCardSide1.setLayoutParams(params);
		mCardSide2.setLayoutParams(params);

		// Set QR code size - Adjust depending on screen size
		int qrCodeSize = Math.round((float) params.height / 3.75f);
		mQrCodeLayout.getLayoutParams().width = qrCodeSize;
		mQrCodeLayout.getLayoutParams().height = qrCodeSize;
		generateQrCode(qrCodeSize);
	}

	@Background
	protected void generateQrCode(int size) {
		QRCodeWriter writer = new QRCodeWriter();
		size *= 3; // otherwise QR code is too small
		try {
			BitMatrix matrix = writer.encode(String.format("%s%s", mBitlyPrefix, mCard.getBitly()), BarcodeFormat.QR_CODE, size, size);
			Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
			for (int i = 0; i < size; i++) { // width
				for (int j = 0; j < size; j++) { // height
					bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
				}
			}

			if (bitmap != null) {
				setQrCode(bitmap);
			}
		} catch (WriterException e) {
			LOGGER.error("", e);
		}
	}

	@UiThread
	protected void setQrCode(Bitmap bitmap) {
		mQrCode.setImageBitmap(bitmap);
		mQrCodeProgressBar.setVisibility(View.GONE);
	}

	private Drawable getCardBackground(int color) {
		float r = 2f; // radius

		GradientDrawable g = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] { color, color });
		g.setGradientType(GradientDrawable.RECTANGLE);
		g.setStroke(mCardPadding, mWhite);
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
		width = width * 85 / 100;
		height = height * 70 / 100;

		// Add some more padding for large devices
		if (width > 860) {
			width = width * 80 / 100;
		}

		// Ratio: width: 1, height: 0.65 (or 0.8 for small screen devices)
		int heightRatio = (width > 350) ? 65 : 80;
		int newHeight = width * heightRatio / 100;
		if (newHeight < height) {
			height = newHeight;
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
