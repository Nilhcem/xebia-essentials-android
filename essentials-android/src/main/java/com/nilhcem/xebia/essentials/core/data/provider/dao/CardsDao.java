package com.nilhcem.xebia.essentials.core.data.provider.dao;

import android.text.TextUtils;

import com.activeandroid.query.Select;
import com.nilhcem.xebia.essentials.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import hugo.weaving.DebugLog;

@Singleton
public class CardsDao {

    public static final String BASE_URL_ESSENTIALS = "http://essentials.xebia.com/";
    private static final String BASE_URL_BITLY = "http://bit.ly/";
    private static final String TITLE_ASC = "title ASC";

    @Inject
    public CardsDao() {
    }

    public boolean hasCards() {
        return new Select().from(Card.class).count() > 0;
    }

    public List<Card> getCards() {
        return new Select().from(Card.class).orderBy(TITLE_ASC).execute();
    }

    @DebugLog
    public Card getCardByUrl(String url) {
        Card found = null;

        if (url.startsWith(BASE_URL_BITLY)) {
            String id = url.replace(BASE_URL_BITLY, "");
            found = getCardByBitlyId(id);
        } else if (url.startsWith(BASE_URL_ESSENTIALS)) {
            String id = url.replace(BASE_URL_ESSENTIALS, "").split("\\?")[0].replaceAll("/", "").toLowerCase(Locale.US);
            found = getCardByUrlId(id);
        }
        return found;
    }

    public Card getCardByUrlId(String id) {
        // URL from the QR code differs from the actual URL
        String url = id;
        if ("uncluttered-build".equals(url)) {
            url = "clean-build";
        } else if ("honour-the-timebox".equals(url)) {
            url = "honor-the-timebox";
        }
        return new Select().from(Card.class).where("url = ?", url).executeSingle();
    }

    private Card getCardByBitlyId(String id) {
        return new Select().from(Card.class).where("bitly = ?", id).executeSingle();
    }

    public List<Card> getCurrentCardsByCategoryId(int categoryId) {
        return new Select().from(Card.class).where("is_deprecated = ? AND category = ?", 0, categoryId).orderBy(TITLE_ASC).execute();
    }

    public Card getRandomCard() {
        return new Select().from(Card.class).orderBy("RANDOM()").executeSingle();
    }

    public List<Card> getCurrentCards() {
        return new Select().from(Card.class).where("is_deprecated = ?", 0).orderBy(TITLE_ASC).execute();
    }

    public List<Card> getCardsFromSearchQuery(String searchQuery) {
        List<Card> cards = new ArrayList<>();

        if (!TextUtils.isEmpty(searchQuery)) {
            String searchTerm = String.format(Locale.US, "%%%s%%", searchQuery.trim());
            cards = new Select().from(Card.class).where("title like ?", searchTerm).execute();

            // If no match, get results that match the summary, or the HTML description
            if (cards.isEmpty()) {
                cards = new Select().from(Card.class).where("summary like ? OR content like ?", searchTerm, searchTerm).execute();
            }
        }
        return cards;
    }
}
