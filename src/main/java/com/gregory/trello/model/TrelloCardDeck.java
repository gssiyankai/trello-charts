package com.gregory.trello.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class TrelloCardDeck implements Iterable<TrelloCard> {

    public static final TrelloCardDeck EMPTY_DECK = new TrelloCardDeck(Collections.<TrelloCard>emptyList());

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

    @Override
    public Iterator<TrelloCard> iterator() {
        return cards.iterator();
    }

    public int size() {
        return cards.size();
    }

    public boolean contains(TrelloCard card) {
        return cards.contains(card);
    }
}
