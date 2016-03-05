package com.gregory.trello.model;

import java.util.List;

public final class TrelloCardDeck {

    private final List<TrelloCard> cards;

    public TrelloCardDeck(List<TrelloCard> cards) {
        this.cards = cards;
    }

    public int points() {
        int points = 0;
        for (TrelloCard card : cards) {
            points += card.points();
        }
        return points;
    }

}
