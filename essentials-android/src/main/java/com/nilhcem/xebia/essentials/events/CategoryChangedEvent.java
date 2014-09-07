package com.nilhcem.xebia.essentials.events;

public class CategoryChangedEvent {

    public final int categoryId;

    public CategoryChangedEvent(int newCategoryId) {
        categoryId = newCategoryId;
    }
}
