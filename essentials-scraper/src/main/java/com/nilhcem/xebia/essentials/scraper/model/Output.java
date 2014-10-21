package com.nilhcem.xebia.essentials.scraper.model;

import java.util.List;

public class Output {

    public final List<OutputCategory> categories;

    public final List<Card> cards;

    public Output(List<OutputCategory> categories, List<Card> cards) {
        this.categories = categories;
        this.cards = cards;
    }

    public static class OutputCategory {

        public final int id;

        public final String color;

        public final String name;

        public OutputCategory(int id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }
    }
}
