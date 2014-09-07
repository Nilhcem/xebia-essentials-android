package com.nilhcem.xebia.essentials.scraper.model;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CategoryTest {

    @Test
    public void should_find_category_from_color() {
        // Given
        String color = "#107FD5";

        // When
        Category category = Category.fromColor(color);

        // Then
        assertThat(category).isEqualTo(Category.REALISATION);
    }

    @Test
    public void should_return_null_when_color_is_invalid() {
        // Given
        String color = "#invalid";

        // When
        Category category = Category.fromColor(color);

        // Then
        assertThat(category).isNull();
    }
}
