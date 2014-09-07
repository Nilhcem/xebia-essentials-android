package com.nilhcem.xebia.essentials.scraper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nilhcem.xebia.essentials.scraper.core.OutputGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {

    public static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("Start scraping");
        Injector injector = Guice.createInjector();
        OutputGenerator generator = injector.getInstance(OutputGenerator.class);
        generator.generate();
        LOG.info("Scraping done");
    }
}
