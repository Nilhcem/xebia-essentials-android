package com.nilhcem.xebia.essentials.scraper.core;

import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains and returns some project-specific configuration variables.
 */
@Singleton
public class Configuration {

    private static final String CONFIG_FILE = "/config.properties";
    private final Properties config = new Properties();

    /**
     * Opens the {@code CONFIG_FILE} file and maps data.
     */
    @Inject
    public Configuration() {
        InputStream in = getClass().getResourceAsStream(CONFIG_FILE);
        try {
            config.load(in);
            in.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(Configuration.class).error("", e);
        }
    }

    /**
     * Returns the value of a specific entry from the {@code CONFIG_FILE} file.
     *
     * @param key a string representing the key from a key/value couple.
     * @return the value of the key, or an empty string if the key was not found.
     */
    public String get(String key) {
        if (config != null && config.containsKey(key)) {
            return config.getProperty(key);
        }
        return "";
    }
}
