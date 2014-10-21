package com.nilhcem.xebia.essentials.scraper.core;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.nilhcem.xebia.essentials.scraper.model.Card;
import com.nilhcem.xebia.essentials.scraper.model.Category;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.nilhcem.xebia.essentials.scraper.AppTest.WIREMOCK_PORT;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ScraperTest {

    private Scraper scraper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);

    @Before
    public void setup() {
        scraper = new Scraper(mock(Configuration.class));
    }

    @Test
    public void should_generate_cards_list_url_when_instantiating() {
        // Given
        Configuration config = mock(Configuration.class);
        when(config.get(Scraper.BASE_URL_KEY)).thenReturn("http://localhost/");
        when(config.get(Scraper.LIST_SUFFIX_KEY)).thenReturn("cards/");

        // When
        scraper = new Scraper(config);

        // Then
        assertThat(scraper.cardsListUrl).isEqualTo("http://localhost/cards/");
    }

    @Test
    public void should_scrape_all_cards_ids() {
        // Given
        scraper.cardsListUrl = "http://localhost:" + WIREMOCK_PORT + "/cards";

        // When
        List<String> cards = scraper.getCardsIds();

        // Then
        assertThat(cards).hasSize(74);
        assertThat(cards).contains("/acceptance-criteria/", "/have-fun/", "/no-multitasking/", "/what-you-measure/");
    }

    @Test
    public void should_return_an_empty_list_when_scraping_fails() {
        // Given
        scraper.cardsListUrl = "http://localhost:" + WIREMOCK_PORT + "/invalid";

        // When
        List<String> cards = scraper.getCardsIds();

        // Then
        assertThat(cards).isEmpty();
    }

    @Test
    public void should_scape_a_card_from_its_id() {
        // Given
        scraper.cardsUrlPrefix = "http://localhost:" + WIREMOCK_PORT;
        String id = "/no-multitasking/";

        // When
        Card card = scraper.getCard(id);

        // Then
        assertThat(card).isNotNull();
        assertThat(card.category).isEqualTo(Category.CRAFTSMANSHIP.ordinal() + 1);
        assertThat(card.url).isEqualTo("no-multitasking");
        assertThat(card.title).isEqualTo("No multitasking");
        assertThat(card.description)
                .contains("Motivation")
                .contains("Your brain does not handle multitasking as well as you think")
                .contains("References")
                .contains("CodingHorror.com");
    }

    @Test
    public void should_return_null_when_id_was_not_found() {
        // Given
        scraper.cardsUrlPrefix = "http://localhost:" + WIREMOCK_PORT;
        String id = "/not-found";

        // When
        Card card = scraper.getCard(id);

        // Then
        assertThat(card).isNull();
    }

    @Test
    public void should_remove_slashes_from_card_id() {
        // Given
        String id1 = "/no-multitasking/";
        String id2 = "have-fun/";
        String id3 = "/poutsma-principle";
        String id4 = "kiss";

        // When
        String result1 = scraper.formatId(id1);
        String result2 = scraper.formatId(id2);
        String result3 = scraper.formatId(id3);
        String result4 = scraper.formatId(id4);

        // Then
        assertThat(result1).isEqualTo("no-multitasking");
        assertThat(result2).isEqualTo("have-fun");
        assertThat(result3).isEqualTo("poutsma-principle");
        assertThat(result4).isEqualTo("kiss");
    }
}
