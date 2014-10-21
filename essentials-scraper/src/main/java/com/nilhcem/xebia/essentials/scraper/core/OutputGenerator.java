package com.nilhcem.xebia.essentials.scraper.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nilhcem.xebia.essentials.scraper.model.Card;
import com.nilhcem.xebia.essentials.scraper.model.Category;
import com.nilhcem.xebia.essentials.scraper.model.Output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String json = gson.toJson(output);

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(createOutputFile());
            out.write(json.getBytes());
            out.close();
        } catch (IOException e) {
            LOG.error("Error saving JSON to file", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("Error closing FileOutputStream", e);
                }
            }
        }
    }

    File createOutputFile() {
        String path = config.get(OUTPUT_FILE_KEY);
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOG.error("Error creating new file: {}", path, e);
        }
        return file;
    }

    List<Output.OutputCategory> getAllCategories() {
        List<Output.OutputCategory> outputCategories = new ArrayList<>();

        for (Category category : Category.values()) {
            outputCategories.add(new Output.OutputCategory(category.ordinal() + 1, capitalizeFirstLetterOnly(category.name()), category.getColor()));
        }
        return outputCategories;
    }

    private List<Card> getAllCards() {
        LOG.debug("Get all cards ids");
        List<String> cardsIds = scraper.getCardsIds();
        LOG.debug("Found {} cards", cardsIds.size());

        List<Card> cards = new ArrayList<>();
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
            LOG.error("Sleep error", e);
        }
    }
}
