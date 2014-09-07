package com.nilhcem.xebia.essentials.events;

public class CardChangedEvent {

    public final int cardPosition;

    public CardChangedEvent(int newCardIndex) {
        cardPosition = newCardIndex;
    }
}
