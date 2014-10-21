package com.nilhcem.xebia.essentials.scraper.core;

import com.nilhcem.xebia.essentials.scraper.model.Card;
import com.nilhcem.xebia.essentials.scraper.model.Category;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {

    private static final Logger LOG = LoggerFactory.getLogger(Scraper.class);

    static final String BASE_URL_KEY = "cards.base.url";
    static final String LIST_SUFFIX_KEY = "cards.list.suffix";

    final Configuration config;
    String cardsUrlPrefix;
    String cardsListUrl;
    Pattern idPattern;

    @Inject
    public Scraper(Configuration config) {
        this.config = config;
        cardsUrlPrefix = config.get(BASE_URL_KEY);
        cardsListUrl = cardsUrlPrefix + config.get(LIST_SUFFIX_KEY);
        idPattern = Pattern.compile("/?([^/]*)/?");
    }

    public List<String> getCardsIds() {
        List<String> cards = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect(cardsListUrl).get();
            Elements elements = doc.select("#main-content ul li a");
            for (Element element : elements) {
                cards.add(element.attr("href").toLowerCase(Locale.US));
            }
        } catch (IOException e) {
            LOG.error("Error getting cards", e);
        }
        return cards;
    }

    public Card getCard(String id) {
        Card card = null;

        try {
            Document doc = Jsoup.connect(getUrlFromId(id)).get();

            // Get title
            Element elemTitle = doc.select("para").first();
            String title = elemTitle.text().trim();

            // Get category
            Element elemColor = doc.select("#masthead").first();
            String color = elemColor.attr("style").replaceAll("background-color:", "").trim();
            Category category = Category.fromColor(color);

            // Get description
            String description = formatDescription(doc.select("#main-content").html().trim());
            card = new Card(formatId(id), title, description, category);
        } catch (IOException e) {
            LOG.error("Error getting card " + id, e);
        }
        return card;
    }

    private String getUrlFromId(String id) {
        return cardsUrlPrefix + id;
    }

    /**
     * Formats the description to make it compatible with Android TextView
     * <p>
     * Android TextViews does not support {@code <ul> or <li>} tags</pre>.
     * </p>
     */
    private String formatDescription(String description) {
        String desc = description
                .replaceAll("<ul>", "")
                .replaceAll("<ol>", "")
                .replaceAll("<li>", "• ")
                .replaceAll("</li>", "<br />")
                .replaceAll("</ul>", "<br />")
                .replaceAll("</ol>", "<br />");

        // Rewrite cards links
        Document doc = Jsoup.parse(desc);
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String href = link.attr("href");
            if (!href.contains(".")) {
                link.attr("href", String.format("%s/%s", cardsUrlPrefix, href));
            }
        }
        desc = doc.select("body").html().replaceAll("[\n\r]", "");

        return desc;
    }

    String formatId(String id) {
        Matcher matcher = idPattern.matcher(id);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return id;
    }
}
