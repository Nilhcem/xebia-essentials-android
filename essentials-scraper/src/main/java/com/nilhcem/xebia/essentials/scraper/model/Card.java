package com.nilhcem.xebia.essentials.scraper.model;

public class Card {

    public final String title;

    public final String url;

    public final String description;

    public final int category;

    public Card(String url, String title, String description, Category category) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.category = category.ordinal() + 1;
    }
}
