package com.nilhcem.xebia.essentials.scraper.model;

import org.fest.assertions.Assertions;
import org.junit.Test;

import static com.nilhcem.xebia.essentials.scraper.model.Category.CRAFTSMANSHIP;

public class CardTest {

    private Card card;

    @Test
    public void should_increment_category_id_before_returning() {
        // Given
        card = new Card("", "", "", CRAFTSMANSHIP);

        // When
        int id = card.getCategoryId();

        // Then
        Assertions.assertThat(id).isEqualTo(CRAFTSMANSHIP.ordinal() + 1);
    }
}
