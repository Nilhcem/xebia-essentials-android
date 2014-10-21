package com.nilhcem.xebia.essentials.scraper.model;

public class Card {

    public final String title;

    public final String id;

    public final String description;

    public final int category;

    public Card(String id, String title, String description, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category.ordinal() + 1;
    }
}
