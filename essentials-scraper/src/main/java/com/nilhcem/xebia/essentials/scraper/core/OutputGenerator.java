package com.nilhcem.xebia.essentials.scraper.core;

import com.nilhcem.xebia.essentials.scraper.model.Card;
import com.nilhcem.xebia.essentials.scraper.model.Category;
import com.nilhcem.xebia.essentials.scraper.model.Output;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OutputGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(OutputGenerator.class);
    private static final int SLEEP_TIME_MS_BETWEEN_EACH_QUERY = 200;
    static final String OUTPUT_FILE_KEY = "output.file";

    Configuration config;
    Scraper scraper;

    @Inject
    public OutputGenerator(Configuration config, Scraper scraper) {
        this.config = config;
        this.scraper = scraper;
    }

    public void generate() {
        Output output = new Output(getAllCategories(), getAllCards());

        Serializer serializer = new Persister();
        try {
            serializer.write(output, createOutputFile());
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    File createOutputFile() {
        File file = new File(config.get(OUTPUT_FILE_KEY));
        return file;
    }

    List<Output.OutputCategory> getAllCategories() {
        List<Output.OutputCategory> outputCategories = new ArrayList<Output.OutputCategory>();

        for (Category category : Category.values()) {
            outputCategories.add(new Output.OutputCategory(category.ordinal() + 1, capitalizeFirstLetterOnly(category.name()), category.getColor()));
        }
        return outputCategories;
    }

    private List<Card> getAllCards() {
        LOG.debug("Get all cards ids");
        List<String> cardsIds = scraper.getCardsIds();
        LOG.debug("Found {} cards", cardsIds.size());

        List<Card> cards = new ArrayList<Card>();
        for (String cardId : cardsIds) {
            sleep();
            LOG.debug("Scrape card {}", cardId);
            cards.add(scraper.getCard(cardId));
        }
        return cards;
    }

    public static String capitalizeFirstLetterOnly(String input) {
        if (input == null || input.length() < 1) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_TIME_MS_BETWEEN_EACH_QUERY);
        } catch (InterruptedException e) {
            LOG.error("", e);
        }
    }
}
