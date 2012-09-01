package com.nilhcem.xebia.essentials.model;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "xebia")
public final class XmlData {
	@ElementList
	private List<Category> categories;

	@ElementList
	private List<Card> cards;

	public List<Category> getCategories() {
		return categories;
	}

	public List<Card> getCards() {
		return cards;
	}
}
