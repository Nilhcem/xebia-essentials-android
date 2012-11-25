package com.nilhcem.xebia.essentials.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.nilhcem.xebia.essentials.R;
import com.nilhcem.xebia.essentials.cards.html.*;
import com.nilhcem.xebia.essentials.core.DatabaseHelper;
import com.nilhcem.xebia.essentials.core.InMemoryCache;
import com.nilhcem.xebia.essentials.core.dao.CardDao;
import com.nilhcem.xebia.essentials.core.model.Card;

@EBean
public class CardScanner {
	private static final Logger LOG = LoggerFactory.getLogger(CardScanner.class);

	@StringRes(R.string.bitly_url_prefix)
	protected String mBitlyPrefix;

	@StringRes(R.string.cards_url_prefix)
	protected String mXebiaEssentialsPrefix;

	@StringRes(R.string.intent_no_card_found)
	protected String mNoCardFound;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Card.class)
	protected CardDao mCardDao;

	@Bean
	protected InMemoryCache mCache;

	public void initiateScan(Activity activity) {
		IntentIntegrator integrator = new IntentIntegrator(activity);
		integrator.initiateScan();
	}

	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			Card card = null;
			String content = scanResult.getContents();
			if (!TextUtils.isEmpty(content)) {
				LOG.debug("QR code found: {}", content);

				if (content.startsWith(mBitlyPrefix)) {
					String bitly = content.substring(mBitlyPrefix.length());
					card = mCardDao.getByBitly(bitly);
				} else {
					String essentialsUrl = String.format("http://%s", mXebiaEssentialsPrefix);
					if (content.startsWith(essentialsUrl)) {
						String url = content.substring(essentialsUrl.length());
						card = mCardDao.getByUrl(url);
					}
				}
			}

			if (card == null) {
				Toast.makeText(activity, mNoCardFound, Toast.LENGTH_SHORT).show();
			} else {
				Intent cardIntent = createIntent(activity, card);
				activity.startActivity(cardIntent);
			}
		}
	}

	public Intent createIntent(Context packageContext, Card card) {
		Intent intent = null;

		if (card != null) {
			Long cardId = card.getId();
			if (cardId != null && cardId > 0) {
				intent = new Intent(packageContext, CardsHtmlActivity_.class);
				intent.putExtra(CardsHtmlActivity.EXTRA_ONE_CARD_ONLY, cardId);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			}
		}
		return intent;
	}
}
