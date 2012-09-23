package com.nilhcem.xebia.essentials.settings;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Window;

public final class AboutCustomDialog extends Dialog {
	private AboutCustomDialogView mView;

	/**
	 * @param dialogType see AboutCustomDialogView.DIALOG_*
	 */
	public AboutCustomDialog(Context context, int dialogType) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mView = new AboutCustomDialogView_(context, dialogType);
		mView.onFinishInflate();

		// Make dialog fill 90% of the screen
		Rect displayRectangle = new Rect();
		Window window = getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		mView.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
	}
}
