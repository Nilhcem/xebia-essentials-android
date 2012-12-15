package com.nilhcem.xebia.essentials.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.TextUtils;

import com.googlecode.androidannotations.annotations.EBean;
import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

@EBean
public class XmlParser {
	private static final String XML_ENCODING = "UTF-8";

	private static final String TAG_CATEGORY = "category";
	private static final String TAG_CATEGORY_ID = "id";
	private static final String TAG_CATEGORY_NAME = "name";
	private static final String TAG_CATEGORY_COLOR = "color";

	private static final String TAG_CARD = "card";
	private static final String TAG_CARD_TITLE = "title";
	private static final String TAG_CARD_CATEGORY = "category";
	private static final String TAG_CARD_BITLY = "bitly";
	private static final String TAG_CARD_URL = "url";
	private static final String TAG_CARD_SUMMARY = "summary";
	private static final String TAG_CARD_CONTENT = "content";

	public XmlData parseXml(InputStream input) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(input, XML_ENCODING);

		XmlData xmlData = new XmlData();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = parser.getName();

				if (name.equals(TAG_CATEGORY)) {
					xmlData.add(readCategory(parser));
				} else if (name.equals(TAG_CARD)) {
					xmlData.add(readCard(parser));
				}
			}
			eventType = parser.next();
		}
		return xmlData;
	}

	private Category readCategory(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, TAG_CATEGORY);

		long catId = Long.parseLong(parser.getAttributeValue(null, TAG_CATEGORY_ID));
		String catName = null;
		String catColor = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() == XmlPullParser.START_TAG) {
				String name = parser.getName();
				if (name.equals(TAG_CATEGORY_NAME)) {
					catName = readText(parser);
				} else if (name.equals(TAG_CATEGORY_COLOR)) {
					catColor = readText(parser);
				} else {
					skip(parser);
				}
			}
		}

		Category category = null;
		if (!TextUtils.isEmpty(catName) && !TextUtils.isEmpty(catColor)) {
			category = new Category(catId, catName, catColor);
		}
		return category;
	}

	private Card readCard(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, TAG_CARD);

		String cardTitle = null;
		long cardCategory = 0l;
		String cardBitly = null;
		String cardUrl = null;
		String cardSummary = null;
		String cardContent = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() == XmlPullParser.START_TAG) {
				String name = parser.getName();
				if (name.equals(TAG_CARD_TITLE)) {
					cardTitle = readText(parser);
				} else if (name.equals(TAG_CARD_CATEGORY)) {
					cardCategory = Long.parseLong(readText(parser));
				} else if (name.equals(TAG_CARD_BITLY)) {
					cardBitly = readText(parser);
				} else if (name.equals(TAG_CARD_URL)) {
					cardUrl = readText(parser);
				} else if (name.equals(TAG_CARD_SUMMARY)) {
					cardSummary = readText(parser);
				} else if (name.equals(TAG_CARD_CONTENT)) {
					cardContent = readText(parser);
				} else {
					skip(parser);
				}
			}
		}

		Card card = null;
		if (!TextUtils.isEmpty(cardTitle)) {
			card = new Card(cardTitle, cardCategory, cardBitly, cardUrl, cardSummary, cardContent);
		}
		return card;
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = null;

		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}

		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
