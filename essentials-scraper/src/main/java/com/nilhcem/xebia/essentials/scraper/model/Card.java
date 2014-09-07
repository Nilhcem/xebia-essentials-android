package com.nilhcem.xebia.essentials.scraper.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "card")
public class Card {

    @Element(name = "title")
    public final String title;

    @Element(name = "url")
    public final String id;

    @Element(name = "description", data = true)
    public final String description;

    public final Category category;

    public Card(String id, String title, String description, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    @Element(name = "category")
    public int getCategoryId() {
        return category.ordinal() + 1;
    }
}
