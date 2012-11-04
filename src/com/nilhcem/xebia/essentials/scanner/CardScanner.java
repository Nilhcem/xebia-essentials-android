package com.nilhcem.xebia.essentials.scanner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.html.CardsHtmlActivity;
import com.nilhcem.xebia.essentials.cards.html.CardsHtmlActivity_;
import com.nilhcem.xebia.essentials.core.bo.CardService;
import com.nilhcem.xebia.essentials.core.model.Card;

@EBean
public class CardScanner {
	private static final Logger LOGGER = LoggerFactory.getLogger(CardScanner.class);

	@StringRes(R.string.bitly_url_prefix)
	protected String mBitlyPrefix;

	@StringRes(R.string.scanner_card_not_found)
	protected String mCardNotFound;

	@Bean
	protected CardService mCardService;

	public void initiateScan(Activity activity) {
		IntentIntegrator integrator = new IntentIntegrator(activity);
		integrator.initiateScan();
	}

	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			Intent cardIntent = null;
			String content = scanResult.getContents();
			if (!TextUtils.isEmpty(content)) {
				LOGGER.debug("QR code found: {}", content);
				if (content.startsWith(mBitlyPrefix)) {
					String bitly = content.substring(mBitlyPrefix.length());
					Card card = mCardService.getDao().getByBitly(bitly);
					cardIntent = createIntent(activity, card);
				}
			}

			if (cardIntent == null) {
				Toast.makeText(activity, mCardNotFound, Toast.LENGTH_SHORT).show();
			} else {
				activity.startActivity(cardIntent);
			}
		}
	}

	public Card checkIntentData(Uri data) {
		Card card = null;
		if (data != null) {
			List<String> params = data.getPathSegments();
			if (params != null && params.size() == 1) {
				String cardUrl = params.get(0);
				card = mCardService.getDao().getByUrl(cardUrl);
			}
		}
		return card;
	}

	public Intent createIntent(Context packageContext, Card card) {
		Intent intent = null;

		if (card != null) {
			Long cardId = card.getId();
			if (cardId != null && cardId > 0) {
				intent = new Intent(packageContext, CardsHtmlActivity_.class);
				intent.putExtra(CardsHtmlActivity.INTENT_ITEM_ID, cardId);
				intent.putExtra(CardsHtmlActivity.INTENT_DISPLAY_TYPE, CardsHtmlActivity.DISPLAY_ONE_CARD);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			}
		}
		return intent;
	}
}
