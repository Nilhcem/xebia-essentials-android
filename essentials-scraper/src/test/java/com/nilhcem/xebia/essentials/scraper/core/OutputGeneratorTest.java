package com.nilhcem.xebia.essentials.scraper.core;

import com.nilhcem.xebia.essentials.scraper.model.Category;
import com.nilhcem.xebia.essentials.scraper.model.Output;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.List;

import static com.nilhcem.xebia.essentials.scraper.model.Category.CRAFTSMANSHIP;
import static org.fest.assertions.Assertions.assertThat;

public class OutputGeneratorTest {

    private OutputGenerator generator;

    @Mock
    private Configuration config;

    @Mock
    private Scraper scraper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        generator = new OutputGenerator(config, scraper);
    }

    @Test
    public void should_return_all_categories_in_a_list() {
        // When
        List<Output.OutputCategory> allCategories = generator.getAllCategories();

        // Then
        assertThat(allCategories).hasSize(Category.values().length);
        Output.OutputCategory firstCategory = allCategories.get(0);
        assertThat(firstCategory.id).isEqualTo(1);
        assertThat(firstCategory.color).isEqualTo(CRAFTSMANSHIP.getColor());
        assertThat(firstCategory.name).isEqualToIgnoringCase(CRAFTSMANSHIP.name());
    }

    @Test
    public void should_capitalize_properly() {
        // Given
        String input = "crAFtsMAnsHIp";

        // When
        String result = OutputGenerator.capitalizeFirstLetterOnly(input);

        // Then
        assertThat(result).isEqualTo("Craftsmanship");
    }

    @Test
    public void should_return_same_string_when_too_small_or_invalid() {
        // Given
        String input1 = null;
        String input2 = "";
        String input3 = "a";
        String input4 = "ab";

        // When
        String result1 = OutputGenerator.capitalizeFirstLetterOnly(input1);
        String result2 = OutputGenerator.capitalizeFirstLetterOnly(input2);
        String result3 = OutputGenerator.capitalizeFirstLetterOnly(input3);
        String result4 = OutputGenerator.capitalizeFirstLetterOnly(input4);

        // Then
        assertThat(result1).isNull();
        assertThat(result2).isEmpty();
        assertThat(result3).isEqualTo("A");
        assertThat(result4).isEqualTo("Ab");
    }

    @Test
    public void should_create_output_file_from_config_file() {
        // Given
        String expectedName = "expected_file_name";
        Mockito.when(config.get(OutputGenerator.OUTPUT_FILE_KEY)).thenReturn(expectedName);

        // When
        File outputFile = generator.createOutputFile();

        // Then
        assertThat(outputFile.getName()).isEqualTo(expectedName);
    }
}
