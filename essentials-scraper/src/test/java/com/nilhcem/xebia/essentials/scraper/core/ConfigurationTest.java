package com.nilhcem.xebia.essentials.scraper.core;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ConfigurationTest {

    private Configuration config = new Configuration();

    @Test
    public void should_get_properties_when_available() {
        // When
        String url = config.get("test.url");
        String other = config.get("test.other.value");

        // Then
        assertThat(url).isEqualTo("http://www.nilhcem.com/");
        assertThat(other).isEqualTo("42");
    }

    @Test
    public void should_return_empty_when_property_is_not_available() {
        // When
        String unavailable = config.get("no.way.this.key.is.available");

        // Then
        assertThat(unavailable).isEmpty();
    }
}
