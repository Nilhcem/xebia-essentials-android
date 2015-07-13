package com.nilhcem.xebia.essentials.ui.cards.detail.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.core.qrcode.QrCodeGenerator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailCardQrCodeView extends FrameLayout {

    @Bind(R.id.detail_card_front_qrcode_image) ImageView mQrCode;
    @Bind(R.id.detail_card_front_qrcode_loading) ProgressBar mProgress;

    private final int mQrCodeSize;

    public DetailCardQrCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detail_card_fragment_qrcode, this, true);
        ButterKnife.bind(this, this);
        mQrCodeSize = context.getResources().getDimensionPixelSize(R.dimen.card_flip_qrcode_size);
    }

    @Override
    protected void finalize() throws Throwable {
        ButterKnife.unbind(this);
        super.finalize();
    }

    public void bindData(String url) {
        new QrCodeGeneratorAsync().execute(url);
    }

    private class QrCodeGeneratorAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mQrCode.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return new QrCodeGenerator().generate(params[0], mQrCodeSize);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mQrCode.setImageBitmap(bitmap);
            mProgress.setVisibility(View.GONE);
            mQrCode.setVisibility(View.VISIBLE);
        }
    }
}
