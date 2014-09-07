package com.nilhcem.xebia.essentials.core.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.model.Card;

import javax.inject.Inject;

import timber.log.Timber;

public class ZxingIntegrator {

    public static final int REQUEST_CODE = IntentIntegrator.REQUEST_CODE;

    @Inject CardsDao mCardsDao;

    public void initiateScan(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.initiateScan();
    }

    public Card onActivityResult(int requestCode, int resultCode, Intent data) {
        Card card = null;

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String content = scanResult.getContents();
            if (!TextUtils.isEmpty(content)) {
                Timber.d("QR code found: %s", content);
                card = mCardsDao.getCardByUrl(content);
            }
        }
        return card;
    }
}
