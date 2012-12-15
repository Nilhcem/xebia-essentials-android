package com.nilhcem.xebia.essentials.xml;

import java.util.ArrayList;
import java.util.List;

import com.nilhcem.xebia.essentials.core.model.Card;
import com.nilhcem.xebia.essentials.core.model.Category;

public final class XmlData {
	private List<Category> mCategories;

	private List<Card> mCards;

	public XmlData() {
		mCategories = new ArrayList<Category>();
		mCards = new ArrayList<Card>();
	}

	public void add(Category category) {
		mCategories.add(category);
	}

	public void add(Card card) {
		mCards.add(card);
	}

	public List<Category> getCategories() {
		return mCategories;
	}

	public List<Card> getCards() {
		return mCards;
	}
}
