package com.nilhcem.xebia.essentials.settings;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.HtmlRes;
import com.nilhcem.xebia.essentials.R;

@EViewGroup(R.layout.about_dialog)
public class AboutCustomDialogView extends LinearLayout {
	private static final String TAG = "AboutCustomDialogView";
	private static final String LICENSES_FILE = "LICENSES";

	public static final int DIALOG_XEBIA = 1;
	public static final int DIALOG_CARDS = 2;
	public static final int DIALOG_LICENSES = 3;

	private int mDialogType; // see DIALOG_*

	@ViewById(R.id.aboutDialogTitle)
	protected TextView mTitle;

	@ViewById(R.id.aboutDialogContent)
	protected TextView mContent;

	@ViewById(R.id.aboutDialogImage)
	protected ImageView mImage;

	@HtmlRes(R.string.about_xebia_content)
	protected Spanned mAboutXebia;

	@HtmlRes(R.string.about_cards_content)
	protected Spanned mAboutCards;

	public AboutCustomDialogView(Context context) {
		super(context);
	}

	public AboutCustomDialogView(Context context, int dialogType) {
		super(context);
		mDialogType = dialogType;
	}

	// Must be named "onFinishInflate" for AndroidAnnotations
	public void onFinishInflate() {
		switch (mDialogType) {
			case DIALOG_XEBIA:
				mTitle.setText(R.string.about_xebia_title);
				mContent.setText(mAboutXebia);
				mImage.setVisibility(View.VISIBLE);
				break;
			case DIALOG_CARDS:
				mTitle.setText(R.string.about_cards_title);
				mContent.setText(mAboutCards);
				mImage.setVisibility(View.GONE);
				mContent.setMovementMethod(LinkMovementMethod.getInstance());
				break;
			case DIALOG_LICENSES:
				mTitle.setVisibility(View.GONE);
				mContent.setText(getLicensesFromAssets());
				mImage.setVisibility(View.GONE);
				break;
		}
	}

	private String getLicensesFromAssets() {
		String licenses;

		try {
			InputStream stream = getContext().getAssets().open(LICENSES_FILE);
			licenses = IOUtils.toString(stream);
		} catch (IOException e) {
			Log.e(TAG, "Error while reading file " + LICENSES_FILE, e);
			licenses = "";
		}
		return licenses;
	}
}
