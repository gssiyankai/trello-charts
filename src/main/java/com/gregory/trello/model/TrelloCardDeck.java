package com.gregory.trello.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.gregory.trello.utils.TrelloUtils.board;

public final class TrelloCardDeck implements Iterable<TrelloCard> {

    public static final TrelloCardDeck EMPTY_DECK = new TrelloCardDeck(Collections.<TrelloCard>emptyList());

    private final List<TrelloCard> cards;

    public TrelloCardDeck() {
        this.cards = new ArrayList<>();
    }

    public TrelloCardDeck(List<TrelloCard> cards) {
        this.cards = cards;
    }

    public TrelloCardDeck addCard(TrelloCard card) {
        this.cards.add(card);
        return this;
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

    public TrelloCardDeck cardsByLabelName(String labelName) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            for (TrelloLabel label : card.labels()) {
                if (labelName.equals(label.name())) {
                    result.add(card);
                }
            }
        }
        return new TrelloCardDeck(result);
    }

    public TrelloCardDeck cardsByListName(String listName) {
        List<TrelloCard> result = new ArrayList<>();
        for (TrelloCard card : cards) {
            String listId = card.listId();
            for (TrelloList list : board().listsByName(listName)) {
                if (list.id().equals(listId)) {
                    result.add(card);
                    break;
                }
            }
        }
        return new TrelloCardDeck(result);
    }

}
