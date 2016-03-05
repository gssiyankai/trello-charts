package com.gregory.trello.charts.history;

import com.gregory.trello.model.TrelloCard;
import com.gregory.trello.model.TrelloCardDeck;

import java.util.*;

import static com.gregory.trello.model.TrelloCardDeck.EMPTY_DECK;
import static com.gregory.trello.utils.TrelloUtils.board;

public final class Day {

    private final Date date;
    private final Map<String, TrelloCardDeck> decks;

    Day(Date date) {
        this.date = date;
        this.decks = new HashMap<>();
        Map<String, List<TrelloCard>> cards = new HashMap<>();
        for (TrelloCard card : board().cardsAt(date)) {
            String listId = card.listIdAt(date);
            String listName = board().listById(listId).name();
            List<TrelloCard> cs = cards.get(listName);
            if (cs == null) {
                cs = new ArrayList<>();
                cards.put(listName, cs);
            }
            cs.add(card);
            cards.put(listName, cs);
        }
        for (Map.Entry<String, List<TrelloCard>> entry : cards.entrySet()) {
            decks.put(entry.getKey(), new TrelloCardDeck(entry.getValue()));
        }
    }

    public Date date() {
        return date;
    }

    TrelloCardDeck cardsInListNamed(String listName) {
        TrelloCardDeck deck = this.decks.get(listName);
        if (deck == null) {
            deck = EMPTY_DECK;
        }
        return deck;
    }

}
