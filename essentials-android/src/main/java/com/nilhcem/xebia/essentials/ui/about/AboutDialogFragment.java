package com.nilhcem.xebia.essentials.ui.about;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nilhcem.xebia.essentials.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutDialogFragment extends DialogFragment {

    public static final String TAG = AboutDialogFragment.class.getSimpleName();

    @Bind(R.id.about_dialog_card_content) TextView mCardContent;
    @Bind(R.id.about_dialog_get_content) TextView mGetContent;
    @Bind(R.id.about_dialog_xebia_content) TextView mXebiaContent;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.about_dialog_fragment, container, false);
        ButterKnife.bind(this, layout);

        mCardContent.setText(Html.fromHtml(getString(R.string.about_cards_content)));
        mCardContent.setMovementMethod(LinkMovementMethod.getInstance());
        mGetContent.setText(Html.fromHtml(getString(R.string.about_get_content)));
        mGetContent.setMovementMethod(LinkMovementMethod.getInstance());
        mXebiaContent.setText(Html.fromHtml(getString(R.string.about_xebia_content)));

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
