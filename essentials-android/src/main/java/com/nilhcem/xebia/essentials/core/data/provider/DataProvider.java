package com.nilhcem.xebia.essentials.core.data.provider;

import com.nilhcem.xebia.essentials.core.data.provider.dao.CardsDao;
import com.nilhcem.xebia.essentials.events.CardChangedEvent;
import com.nilhcem.xebia.essentials.events.CategoryChangedEvent;
import com.nilhcem.xebia.essentials.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_ALL;
import static com.nilhcem.xebia.essentials.model.Category.CATEGORY_ID_SEARCH;

@Singleton
public class DataProvider {

    public static final int CARD_POSITION_UNSET = -1;

    /* Contains the cards that should be displayed to the user (when a category is selected, the list content changes too) */
    private List<Card> mVisibleCards;

    /* card view (true) or info view (false) */
    private final AtomicBoolean mDisplayCardView = new AtomicBoolean(true);

    private final AtomicInteger mCurrentCategoryId = new AtomicInteger(CATEGORY_ID_ALL);
    private final AtomicInteger mCurrentCardPosition = new AtomicInteger(CARD_POSITION_UNSET);

    @Inject CardsDao mCardsDao;
    @Inject protected EventBus mEventBus;

    public void initData() {
        mVisibleCards = new ArrayList<>(mCardsDao.getCurrentCards());
    }

    public List<Card> getCards() {
        return mVisibleCards;
    }

    public int getCurrentCardPosition(boolean includeUnsetValue) {
        int position = mCurrentCardPosition.get();
        if (!includeUnsetValue && position == CARD_POSITION_UNSET) {
            position = 0;
        }
        return position;
    }

    @DebugLog
    public void setCurrentCardPosition(int newPosition) {
        int oldPosition = mCurrentCardPosition.getAndSet(newPosition);
        if (oldPosition != newPosition) {
            mEventBus.post(new CardChangedEvent(newPosition));
        }
    }

    public int getCurrentCategoryId() {
        return mCurrentCategoryId.get();
    }

    @DebugLog
    public void setCurrentCategoryId(int newCategoryId) {
        int oldCategoryId = mCurrentCategoryId.getAndSet(newCategoryId);

        if (oldCategoryId != newCategoryId) {
            List<Card> cards;
            if (newCategoryId == CATEGORY_ID_ALL) {
                cards = mCardsDao.getCards();
            } else {
                cards = mCardsDao.getCurrentCardsByCategoryId(newCategoryId);
            }

            if (cards != null) {
                mCurrentCardPosition.set(CARD_POSITION_UNSET);
                mVisibleCards.clear();
                mVisibleCards.addAll(cards);
            }
            mEventBus.post(new CategoryChangedEvent(newCategoryId));
        }
    }

    @DebugLog
    public void resetData() {
        if (mCurrentCategoryId.getAndSet(CATEGORY_ID_ALL) != CATEGORY_ID_ALL) {
            mVisibleCards.clear();
            mVisibleCards.addAll(mCardsDao.getCurrentCards());
        }
        mCurrentCardPosition.set(CARD_POSITION_UNSET);
    }

    public void switchDisplayMode() {
        mDisplayCardView.set(!mDisplayCardView.get());
    }

    public boolean shouldDisplayCardView() {
        return mDisplayCardView.get();
    }

    public void setCardsFromSearchResult(List<Card> cards) {
        mCurrentCardPosition.set(CARD_POSITION_UNSET);
        mVisibleCards.clear();
        mVisibleCards.addAll(cards);
        mCurrentCategoryId.set(CATEGORY_ID_SEARCH);
    }
}
