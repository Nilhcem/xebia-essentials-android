package com.nilhcem.xebia.essentials.scraper.model;

public enum Category {

    CRAFTSMANSHIP("#F80068"),
    COLLABORATION("#FC7A25"),
    REALISATION("#107FD5"),
    TESTING("#6DC726");

    private String color;

    private Category(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static Category fromColor(String color) {
        for (Category curCategory : Category.values()) {
            if (color.equalsIgnoreCase(curCategory.color)) {
                return curCategory;
            }
        }
        return null;
    }
}
