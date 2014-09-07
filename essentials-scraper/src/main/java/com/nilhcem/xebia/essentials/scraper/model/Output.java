package com.nilhcem.xebia.essentials.scraper.model;

import org.simpleframework.xml.*;

import java.util.List;

@Root(name = "xebia")
public class Output {

    @Path("categories")
    @ElementList(inline = true)
    public final List<OutputCategory> categories;

    @Path("cards")
    @ElementList(inline = true)
    public final List<Card> cards;

    public Output(List<OutputCategory> categories, List<Card> cards) {
        this.categories = categories;
        this.cards = cards;
    }

    @Root(name = "category")
    public static class OutputCategory {

        @Attribute(name = "id")
        public final int id;

        @Attribute(name = "color")
        public final String color;

        @Text
        public final String name;

        public OutputCategory(int id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }
    }
}
